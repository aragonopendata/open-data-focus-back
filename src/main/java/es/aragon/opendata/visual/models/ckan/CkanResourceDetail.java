package es.aragon.opendata.visual.models.ckan;

import com.google.gson.JsonElement;

public class CkanResourceDetail {

    private String format;
    private JsonElement data;

    public CkanResourceDetail() {
    }

    public CkanResourceDetail(String format, JsonElement data) {
        this.format = format;
        this.data = data;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format.toUpperCase();
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }
}
