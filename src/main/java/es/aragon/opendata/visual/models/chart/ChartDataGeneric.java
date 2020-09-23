package es.aragon.opendata.visual.models.chart;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "charts")
public class ChartDataGeneric {

    @Id
    private String id;

    private String type;
    private Boolean isMap;
    private String title;
    private NumberChart number;
    private List<ChartData> data;
    private List<String> labels;
    private List<String> descriptions;
    private Float width;
    private Date date;

    public ChartDataGeneric() {
        this.date = new Date();
    }

    public List<ChartData> getData() {
        return data;
    }

    public List<String> getLabels() {
        return labels;
    }

    public String getType() {
        return type;
    }

    public void setData(List<ChartData> data) {
        this.data = data;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public Float getWidth() { return width; }

    public void setWidth(Float width) { this.width = width; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public void setDate(Date date) { this.date = date; }

    public Boolean getMap() { return isMap; }

    public void setMap(Boolean map) { isMap = map; }

    public List<String> getDescriptions() { return descriptions; }

    public void setDescriptions(List<String> descriptions) { this.descriptions = descriptions; }

    public Boolean getIsMap() {
        return isMap;
    }

    public void setIsMap(Boolean isMap) {
        this.isMap = isMap;
    }

    public NumberChart getNumber() {
        return number;
    }

    public void setNumber(NumberChart number) {
        this.number = number;
    }
    
}