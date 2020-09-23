package es.aragon.opendata.visual.rest.services.ckan.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import es.aragon.opendata.visual.models.ckan.CkanResourceDetailList;
import es.aragon.opendata.visual.rest.services.ckan.RestServiceEndpoint;
import es.aragon.opendata.visual.rest.services.ckan.internal.DataTransform;
import es.aragon.opendata.visual.util.Cookies;
import es.aragon.opendata.visual.util.Utils;
import es.aragon.opendata.visual.util.Constants;

public class RestServiceEndpointImpl implements RestServiceEndpoint {

    private static final ResourceBundle config = ResourceBundle.getBundle("application");
    private static final Logger log = LogManager.getLogger();

    @Override
    public Response packageList() {
        log.info("--- Peticion GET PackageList CKAN ---");

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(config.getString("ckan.api.url"));

        target = target.path("api/3/action/package_search").queryParam("q", "res_format:(CSV OR px OR PX)").queryParam("rows",
                "0");

        Gson gson = new GsonBuilder().setLenient().create();
        JsonObject jsonResponse = gson.fromJson(target.request().get(String.class), JsonObject.class);
        int resultNumber = Integer.parseInt(jsonResponse.get("result").getAsJsonObject().get("count").getAsString());

        String response = "";
        int cant = 1000;
        for (int i = 0; i < resultNumber; i += 1000) {
            if (i != 0) {
                response += ",";
            }
            if (i + 1000 > resultNumber)
                cant = resultNumber - i;

            target = client.target(config.getString("ckan.api.url"));
            target = target.path("api/search/dataset").queryParam("q", "res_format:(CSV OR px OR PX)").queryParam("start", i)
                    .queryParam("rows", cant).queryParam("fl", "name,title");
            response += target.request().get(String.class);
        }

        // https://opendata.aragon.es/ckan/api/search/dataset?q=res_format:(CSV)&start=0&rows=1000&fl=name,title
        return Response.ok("[" + response + "]").build();
    }

    @Override
    public Response packageResource(JsonObject input) {
    	
       log.info("--- Peticion POST packageResource --" + input.get("format")  + " ---");

       CkanResourceDetailList jsonArrayResponse = new CkanResourceDetailList();
       
       try {
	       if (input.get("url").getAsString().contains("saw.dll")) {
	    	   
	    	   Cookies cookies = new Cookies();
	    	  
	    	   Map<String, String> headers = new HashMap<String, String>();
	    	   headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36");
	    	   headers.put("Cookie", "sawU=" + config.getString("bi.oracle.sawUiAragonBiAragon") + 
	    			   "; ORA_BIPS_LBINFO=" + config.getString("bi.oracle.oraBipsLbinfoBiAragon") + 
	    			   "; ORA_BIPS_NQID=" + config.getString("bi.oracle.oraBipsNqidBiAragon") + 
	    			   "; __utma=" + config.getString("bi.oracle.utmaBiAragon") + 
	    			   "; __utmc=" + config.getString("bi.oracle.utmcBiAragon") + 
	    			   "; __utmz=" + config.getString("bi.oracle.utmzBiAragon"));
	    	   headers.put("content-type", "text/csv; charset=ISO-8859-1");
				
	    	   Utils.processURLGet( config.getString("bi.oracle.urlBiAragon") + config.getString("bi.oracle.initialDataCube") + "&Action=Download&Options=df&NQUser=granpublico&NQPassword=granpublico" , "", headers, cookies, "ISO-8859-1");
	    	   
	    	   String content = Utils.processURLGet(input.get("url").getAsString(), "", headers, cookies, "ISO-8859-1");
	    	   
	    	   if (Utils.v(content)) {
					content = cleanAndTransform(content);
					if (!content.contains(Constants.errorDoctypeHtml1) && !content.contains(Constants.errorHtml) && !content.contains(Constants.errorDoctypeHtml2) && !content.contains(Constants.errorDiv) && !content.contains(Constants.errorNingunaFila)) {
						jsonArrayResponse.add(DataTransform.csvToJson(content));
					} 
				}
				
	       } else  {
	    	   
	    	   Client client = ClientBuilder.newClient();
	           WebTarget target = client.target(input.get("url").getAsString());
               Response object = target.request().get();
               InputStream is = new BufferedInputStream((InputStream) object.getEntity());
               
               CharsetDetector detector = new CharsetDetector();
    	       detector.setText(is);
    	       CharsetMatch cm = detector.detect();
    	       InputStreamReader resource = new InputStreamReader(is, Charset.forName(cm.getName()));
    	       
    	       switch (input.get("format").getAsString()) {
    		       case "CSV":
    		           jsonArrayResponse.add(DataTransform.csvToJson(IOUtils.toString(resource)));
    		           break;
    		       case "PX":
    		           jsonArrayResponse.add(DataTransform.pxToJson(IOUtils.toString(resource)));
    		           break;
    		       default:
    	       }
    	       
	       }
	       
       } catch (Exception e) {
           log.error("Error", e);
       }
       
       return Response.ok(jsonArrayResponse).build();
    }

    @Override
    public Response packageInfo(JsonObject input) {
        log.info("--- Peticion POST PackageInfo " + input.getAsJsonArray("packages").get(0).getAsString() + " ---");

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(config.getString("ckan.api.url"));

        target = target.path("api/3/action/package_show").queryParam("id",
                input.getAsJsonArray("packages").get(0).getAsString());

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
        JsonObject jsonResponse = gson.fromJson(resource, JsonObject.class);

        return Response.ok(jsonResponse).build();
    }
    
    private String cleanAndTransform(String content) {
		content = content.replace(new String(Character.toChars(0)), "");
		content = content.replace("ÿþ", "");
		return content;
	}
}