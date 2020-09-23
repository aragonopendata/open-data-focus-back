package es.aragon.opendata.visual.rest.services.gaodc.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ResourceBundle;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;

import es.aragon.opendata.visual.rest.services.gaodc.RestServiceEndpoint;

public class RestServiceEndpointImpl implements RestServiceEndpoint {

    private static final ResourceBundle config = ResourceBundle.getBundle("application");
    private static final Logger log = LogManager.getLogger();

    @Override
    public Response packageList() {
        log.info("--- Peticion GET PackageList GAODC---");

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(config.getString("gaodc.api.url"));

        target = target.path("views");
        Invocation.Builder builder = target.request();
        String response = builder.get(String.class);

        return Response.ok(response).build();
    }

    @Override
    public Response packageInfo(JsonObject input) {
        log.info("--- Peticion POST PackageInfo " + input.getAsJsonPrimitive("packages") + " ---");

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(config.getString("gaodc.api.url"));

        target = target.path("preview").queryParam("view_id", input.getAsJsonPrimitive("packages"));

        InputStream is = new BufferedInputStream((InputStream) target.request().get().getEntity());
        CharsetDetector detector = new CharsetDetector();
        try {
            detector.setText(is);
        } catch (IOException e1) {
            log.error("Error", e1);
        }
        CharsetMatch cm = detector.detect();
        InputStreamReader resource = new InputStreamReader(is, Charset.forName(cm.getName()));

        Gson gson = new GsonBuilder().setLenient().create();
        JsonArray jsonResponse = gson.fromJson(resource, JsonArray.class);

        return Response.ok(jsonResponse).build();
    }
}