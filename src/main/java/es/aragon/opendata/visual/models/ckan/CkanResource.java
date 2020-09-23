package es.aragon.opendata.visual.models.ckan;

public class CkanResource {

    private static final String REPLACE = "[\",\']";

    private String format;
    private String url;
    private String name;

    public CkanResource(String format, String url) {
        this.format = format.toUpperCase().replaceAll(REPLACE, "");
        this.url = url.replaceAll(REPLACE, "");
    }

    public CkanResource() {
        //EMPTY CONTRUCTOR
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format.toUpperCase().replaceAll(REPLACE, "");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url.replaceAll(REPLACE, "");
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" + "format='" + format + '\'' + ", URL='" + url + '\'' + '}';
    }
}
