package es.aragon.opendata.visual.rest.services.virtuoso.impl;
import com.google.gson.JsonObject;
import es.aragon.opendata.visual.rest.services.virtuoso.RestServiceEndpoint;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ResourceBundle;

public class RestServiceEndpointImpl implements RestServiceEndpoint {

    private static final ResourceBundle config = ResourceBundle.getBundle("application");
    private static final Logger log = LogManager.getLogger();

    @Override
    public Response packageInfo(JsonObject input) {
        log.info("--- Peticion POST PackageInfo VIRTUOSO ---");

        String query = input.get("packages").getAsString();

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(config.getString("virtuoso.api.url"));

        target = target.path("sparql").queryParam("query", query).queryParam("format", "application/json").queryParam("default-graph-uri", "");

        String theString = "";
        try {
            InputStream is = new BufferedInputStream((InputStream) target.request().get().getEntity());
            CharsetDetector detector = new CharsetDetector();
            detector.setText(is);
            CharsetMatch cm = detector.detect();
            InputStreamReader resource = new InputStreamReader(is, Charset.forName(cm.getName()));

            theString = IOUtils.toString(resource);
            
        } catch (IOException e) {
            log.error("Excepcion producida", e);
        }

        return Response.ok(theString).build();
    }
}