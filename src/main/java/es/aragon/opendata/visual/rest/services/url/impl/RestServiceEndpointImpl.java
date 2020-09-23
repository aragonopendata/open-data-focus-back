package es.aragon.opendata.visual.rest.services.url.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import es.aragon.opendata.visual.models.ckan.CkanResourceDetail;
import es.aragon.opendata.visual.models.ckan.CkanResourceDetailList;
import es.aragon.opendata.visual.rest.services.ckan.internal.DataTransform;
import es.aragon.opendata.visual.rest.services.url.RestServiceEndpoint;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;
import org.springframework.boot.json.JsonParser;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.concurrent.*;

public class RestServiceEndpointImpl implements RestServiceEndpoint {

    private static final Logger log = LogManager.getLogger();

    @Override
    public Response packageInfo(JsonObject input) {
        log.info("--- Peticion POST PackageInfo URL ---");

        String url = input.get("packages").getAsString();

        CkanResourceDetailList jsonArrayResponse = new CkanResourceDetailList();

        Client client = ClientBuilder.newClient();

        WebTarget target = client.target(encodeURLParams(url));

        Response respuesta = null;
        int error = 0;
        String errorMessage = "Error";
        try {
            // schedule the work
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<Response> future = executor.submit(new Task(target));

            try {
                // where we wait for task to complete
                respuesta = future.get(30, TimeUnit.SECONDS);

                String contentType = respuesta.getHeaderString("Content-Type");

                InputStream is = new BufferedInputStream((InputStream) respuesta.getEntity());
                
                CharsetDetector detector = new CharsetDetector();
                detector.setText(is);
                CharsetMatch cm = detector.detect();
                InputStreamReader resource = new InputStreamReader(is, Charset.forName(cm.getName()));
                try {
                    if (contentType.contains("text/csv"))
                        jsonArrayResponse.add(DataTransform.csvToJson(IOUtils.toString(resource)));
                    else if (url.substring(url.length() - 2).toLowerCase().compareTo("px") == 0)
                        jsonArrayResponse.add(DataTransform.pxToJson(IOUtils.toString(resource)));
                    else {
                        error = 1;
                        errorMessage = "{\"Error\": \"Not CSV or PX\"}";
                    }
                } catch (IOException e) {
                    log.error(errorMessage, e);
                    error = 1;
                    errorMessage = "{\"Error\": \"Not CSV or PX\"}";
                }
            } catch (Exception e) {
                log.info(errorMessage, e);
                future.cancel(true /* mayInterruptIfRunning */);
                error = 2;
                errorMessage = "{\"errorMessage\": \"El fichero es demasiado pesado\"}";
            }

        } catch (JsonSyntaxException e) {
            error = 1;
            log.error(errorMessage, e);
        }

        if (error > 0) {
            JsonElement datas = new com.google.gson.JsonParser().parse(errorMessage);
            jsonArrayResponse.add(new CkanResourceDetail(errorMessage, datas));
        }

        return Response.ok(jsonArrayResponse).build();
    }

    public static String encodeURLParams(String query) {
        String baseUrl = query;
        if (query.indexOf("&") != -1) {
            baseUrl = query.substring(0, query.indexOf("&"));
            query = query.substring(query.indexOf("&") + 1);
            String[] params = query.split("&");
            String url;
            try {
                for (String param : params) {
                    String name = param.split("=")[0];
                    String value;
                    try {
                        value = URLEncoder.encode(URLDecoder.decode(param.split("=")[1], "UTF-8"), "UTF-8");
                    }catch (ArrayIndexOutOfBoundsException e){
                        value = "";
                    }
                    if (value.compareTo("") == 0){
                        baseUrl = baseUrl.concat("&" + name);
                    }else{
                        baseUrl = baseUrl.concat("&" + name + "=" + value);
                    }
                }
            } catch (Exception e) {
                log.info("Cant Decode or encode", e);
                baseUrl = "";
            }
        }
        return baseUrl;
    }
}

class Task implements Callable<Response> {
    WebTarget target;

    public Task(WebTarget target) {
        this.target = target;
    }

    @Override
    public Response call() throws Exception {
        return target.request().get();
    }
}