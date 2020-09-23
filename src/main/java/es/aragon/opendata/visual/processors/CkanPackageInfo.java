package es.aragon.opendata.visual.processors;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import es.aragon.opendata.visual.models.ckan.CkanResource;

public class CkanPackageInfo {

    private CkanPackageInfo() {
    }

    public static Multimap<String, CkanResource> processResponse(JsonObject response) {
        JsonArray resources = response.getAsJsonObject("result").getAsJsonArray("resources");
        Multimap<String, CkanResource> multimap = ArrayListMultimap.create();

        for (JsonElement resource : resources) {
            switch (resource.getAsJsonObject().get("format").getAsString().toUpperCase()) {
            case "CSV":
            case "PX":
                CkanResource cr = createCkanResource(resource.getAsJsonObject().get("format").getAsString().toUpperCase(), resource.getAsJsonObject().get("name").getAsString(), resource.getAsJsonObject().get("url").getAsString());
                multimap.put(resource.getAsJsonObject().get("name").getAsString(),cr);
                break;
            default:
            }
        }

        return multimap;
    }

    static CkanResource createCkanResource(String format, String name, String url) {
        CkanResource cr = new CkanResource();
        cr.setFormat(format);
        cr.setName(name);
        cr.setUrl(url);
        return cr;
    }

}