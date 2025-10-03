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

        try {
            Client client = ClientBuilder.newClient();
            String ckanApiUrl = config.getString("ckan.api.url");
            log.info("CKAN API URL: {}", ckanApiUrl);

            // Step 1: Get total count using modern API
            WebTarget target = client.target(ckanApiUrl);
            target = target.path("api/3/action/package_search")
                    .queryParam("q", "res_format:(CSV OR px OR PX)")
                    .queryParam("rows", "0");

            String countUrl = target.getUri().toString();
            log.info("Fetching package count from: {}", countUrl);

            javax.ws.rs.core.Response countResponse = target.request().get();
            int statusCode = countResponse.getStatus();
            log.info("Count request status code: {}", statusCode);

            if (statusCode != 200) {
                log.error("Error fetching package count. Status: {}, Response: {}", statusCode, countResponse.readEntity(String.class));
                return Response.status(statusCode).entity("{\"error\":\"Failed to fetch package count from CKAN\"}").build();
            }

            String countResponseBody = countResponse.readEntity(String.class);
            log.debug("Count response body: {}", countResponseBody);

            Gson gson = new GsonBuilder().setLenient().create();
            JsonObject jsonResponse = gson.fromJson(countResponseBody, JsonObject.class);
            int resultNumber = Integer.parseInt(jsonResponse.get("result").getAsJsonObject().get("count").getAsString());
            log.info("Total packages found: {}", resultNumber);

            // Step 2: Fetch all packages in batches using modern API
            // Match legacy API response format: array of objects with "results" property
            StringBuilder responseBuilder = new StringBuilder();
            int pageSize = 1000; // Modern API max is 1000
            boolean firstPage = true;

            for (int i = 0; i < resultNumber; i += pageSize) {
                int currentPageSize = Math.min(pageSize, resultNumber - i);

                // Use modern CKAN API v3 (package_search instead of deprecated api/search/dataset)
                target = client.target(ckanApiUrl);
                target = target.path("api/3/action/package_search")
                        .queryParam("q", "res_format:(CSV OR px OR PX)")
                        .queryParam("start", i)
                        .queryParam("rows", currentPageSize);

                String pageUrl = target.getUri().toString();
                log.info("Fetching page {}-{} from: {}", i, i + currentPageSize - 1, pageUrl);

                javax.ws.rs.core.Response pageResponse = target.request().get();
                int pageStatusCode = pageResponse.getStatus();

                if (pageStatusCode != 200) {
                    log.error("Error fetching page {}-{}. Status: {}", i, i + currentPageSize - 1, pageStatusCode);
                    return Response.status(pageStatusCode).entity("{\"error\":\"Failed to fetch package list page from CKAN\"}").build();
                }

                String pageBody = pageResponse.readEntity(String.class);
                JsonObject pageJson = gson.fromJson(pageBody, JsonObject.class);

                // Extract results array from modern API response structure
                if (pageJson.has("result") && pageJson.get("result").getAsJsonObject().has("results")) {
                    com.google.gson.JsonArray results = pageJson.get("result").getAsJsonObject().get("results").getAsJsonArray();
                    com.google.gson.JsonArray filteredResults = new com.google.gson.JsonArray();

                    // Filter to only name and title fields for backward compatibility
                    for (int j = 0; j < results.size(); j++) {
                        JsonObject dataset = results.get(j).getAsJsonObject();
                        JsonObject filteredDataset = new JsonObject();

                        if (dataset.has("name")) {
                            filteredDataset.addProperty("name", dataset.get("name").getAsString());
                        }
                        if (dataset.has("title")) {
                            filteredDataset.addProperty("title", dataset.get("title").getAsString());
                        }

                        filteredResults.add(filteredDataset);
                    }

                    // Build response in legacy API format: {"count": X, "results": [...]}
                    if (!firstPage) {
                        responseBuilder.append(",");
                    }
                    firstPage = false;

                    JsonObject legacyFormatPage = new JsonObject();
                    legacyFormatPage.addProperty("count", resultNumber);
                    legacyFormatPage.add("results", filteredResults);
                    responseBuilder.append(gson.toJson(legacyFormatPage));

                    log.info("Successfully fetched page {}-{}, {} results", i, i + currentPageSize - 1, results.size());
                }
            }

            String finalResponse = "[" + responseBuilder.toString() + "]";
            log.info("PackageList completed successfully. Total response length: {}", finalResponse.length());
            return Response.ok(finalResponse).build();

        } catch (Exception e) {
            log.error("Error in packageList", e);
            return Response.status(500).entity("{\"error\":\"Internal server error: " + e.getMessage() + "\"}").build();
        }
    }

    /*
     * DEPRECATED IMPLEMENTATION (Legacy API)
     * ------------------------------------------
     * This is the old implementation using the deprecated CKAN legacy API endpoint.
     * Kept for reference. The legacy API /api/search/dataset is no longer supported
     * in newer CKAN versions.
     *
     * @Deprecated Use modern API implementation above
     */
    /*
    @Override
    public Response packageList_LEGACY() {
        log.info("--- Peticion GET PackageList CKAN (LEGACY) ---");

        try {
            Client client = ClientBuilder.newClient();
            String ckanApiUrl = config.getString("ckan.api.url");
            log.info("CKAN API URL: {}", ckanApiUrl);

            WebTarget target = client.target(ckanApiUrl);
            target = target.path("api/3/action/package_search").queryParam("q", "res_format:(CSV OR px OR PX)").queryParam("rows", "0");

            String countUrl = target.getUri().toString();
            log.info("Fetching package count from: {}", countUrl);

            javax.ws.rs.core.Response countResponse = target.request().get();
            int statusCode = countResponse.getStatus();
            log.info("Count request status code: {}", statusCode);

            if (statusCode != 200) {
                log.error("Error fetching package count. Status: {}, Response: {}", statusCode, countResponse.readEntity(String.class));
                return Response.status(statusCode).entity("{\"error\":\"Failed to fetch package count from CKAN\"}").build();
            }

            String countResponseBody = countResponse.readEntity(String.class);
            log.debug("Count response body: {}", countResponseBody);

            Gson gson = new GsonBuilder().setLenient().create();
            JsonObject jsonResponse = gson.fromJson(countResponseBody, JsonObject.class);
            int resultNumber = Integer.parseInt(jsonResponse.get("result").getAsJsonObject().get("count").getAsString());
            log.info("Total packages found: {}", resultNumber);

            String response = "";
            int cant = 1000;
            for (int i = 0; i < resultNumber; i += 1000) {
                if (i != 0) {
                    response += ",";
                }
                if (i + 1000 > resultNumber)
                    cant = resultNumber - i;

                target = client.target(ckanApiUrl);
                // DEPRECATED: /api/search/dataset is legacy API, not supported in CKAN 2.5+
                target = target.path("api/search/dataset").queryParam("q", "res_format:(CSV OR px OR PX)").queryParam("start", i)
                        .queryParam("rows", cant).queryParam("fl", "name,title");

                String pageUrl = target.getUri().toString();
                log.info("Fetching page {}-{} from: {}", i, i + cant - 1, pageUrl);

                javax.ws.rs.core.Response pageResponse = target.request().get();
                int pageStatusCode = pageResponse.getStatus();

                if (pageStatusCode != 200) {
                    log.error("Error fetching page {}-{}. Status: {}", i, i + cant - 1, pageStatusCode);
                    return Response.status(pageStatusCode).entity("{\"error\":\"Failed to fetch package list page from CKAN\"}").build();
                }

                String pageBody = pageResponse.readEntity(String.class);
                response += pageBody;
                log.info("Successfully fetched page {}-{}, response length: {}", i, i + cant - 1, pageBody.length());
            }

            log.info("PackageList completed successfully. Total response length: {}", response.length());
            return Response.ok("[" + response + "]").build();

        } catch (Exception e) {
            log.error("Error in packageList", e);
            return Response.status(500).entity("{\"error\":\"Internal server error: " + e.getMessage() + "\"}").build();
        }
    }
    */

    @Override
    public Response packageResource(JsonObject input) {

       log.info("--- Peticion POST packageResource --" + input.get("format")  + " ---");
       log.info("Input data - URL: {}, Format: {}",
               input.has("url") ? input.get("url").getAsString() : "null",
               input.has("format") ? input.get("format").getAsString() : "null");

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
           log.error("Error processing packageResource - URL: {}, Format: {}",
                   input.has("url") ? input.get("url").getAsString() : "null",
                   input.has("format") ? input.get("format").getAsString() : "null",
                   e);
           return Response.status(500)
                   .entity("{\"error\": \"Failed to process resource: " + e.getMessage() + "\"}")
                   .build();
       }

       log.info("PackageResource completed successfully. Result count: {}",
               jsonArrayResponse.getResult() != null ? jsonArrayResponse.getResult().size() : 0);
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