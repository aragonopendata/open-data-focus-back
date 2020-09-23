package es.aragon.opendata.visual.models.chart;

import java.util.List;

public class ChartData {

    private String label;
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void addData(String element) {
        this.data.add(element);
    }
}