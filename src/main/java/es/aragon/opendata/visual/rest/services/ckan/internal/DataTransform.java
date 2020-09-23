package es.aragon.opendata.visual.rest.services.ckan.internal;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.XML;

import es.aragon.opendata.visual.models.ckan.CkanResourceDetail;

public class DataTransform {

    private static final Logger log = LogManager.getLogger();

    private static final String CSV_DATA = "CSV";
    private static final String JSON_DATA = "JSON";
    private static final String XML_DATA = "XML";
    private static final String PX_DATA = "PX";

    private DataTransform() {
    }

    public static CkanResourceDetail jsonToJson(String data) {

        CkanResourceDetail crd = new CkanResourceDetail();

        crd.setFormat(JSON_DATA);
        crd.setData(new JsonParser().parse(data).getAsJsonArray());

        return crd;
    }

    public static CkanResourceDetail csvToJson(String data) {

        CkanResourceDetail crd = new CkanResourceDetail();

        CsvParserSettings settings = new CsvParserSettings();
        settings.setDelimiterDetectionEnabled(true);
        settings.setMaxCharsPerColumn(-1);

        CsvParser parser = new CsvParser(settings);
        InputStream is = IOUtils.toInputStream(data, Charset.defaultCharset());
        List<String[]> rows = parser.parseAll(is);

        JsonArray ja = new JsonArray(1);

        for (int i = 1; i < rows.size(); i++) {
            JsonObject jo = new JsonObject();
            for (int j = 0; j < rows.get(0).length; j++) {
                if(rows.get(i)[j] == null)
                    rows.get(i)[j] = "";
                if (rows.get(i)[j] != null && rows.get(0)[j] != null) {
                    jo.addProperty(rows.get(0)[j], rows.get(i)[j]);
                }
            }
            ja.add(jo.getAsJsonObject());
        }

        crd.setFormat(CSV_DATA);
        crd.setData(ja);

        return crd;
    }

    public static CkanResourceDetail xmlToJson(String data) {

        CkanResourceDetail crd = new CkanResourceDetail();

        JSONObject xmlAux = XML.toJSONObject(data);

        crd.setFormat(XML_DATA);
        crd.setData(new JsonParser().parse(xmlAux.toString()));

        return crd;
    }

    public static CkanResourceDetail pxToJson(String data) {

        CkanResourceDetail crd = new CkanResourceDetail();
        crd.setFormat(PX_DATA);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonString = objectMapper.writeValueAsString(data);
            crd.setData(new JsonParser().parse(jsonString));
        } catch (JsonProcessingException e) {
            log.error("Excepcion producida", e);
        }

        return crd;
    }
}