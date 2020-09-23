package es.aragon.opendata.visual.models.chart;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "chartGenerateProcess")
public class ChartGenerateProcess {

    @Id
    private String id;

    private String chartDataId;
    private String title;
    private String typeOfData;
    private String url;
    private String dataset;
    private String chartType;
    private Boolean isMap;
    private NumberChart numberchart;
    private String fieldOrder;
    private Integer sortOrder;
    private List<String> columnsLabel;
    private List<String> columnsData;
    private List<String> columnsDescription;
    private List<ChartLegend> legend;
    private Float widthGraph;
    private Integer topRows;
    private String groupRow;
    private Integer axisXActivator;
    private Date date;
    private String ckanDataset;

    public ChartGenerateProcess() {
        this.id = "-1";
        this.date = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChartDataId() {
        return chartDataId;
    }

    public void setChartDataId(String chartDataId) {
        this.chartDataId = chartDataId;
    }

    public String getTypeOfData() {
        return typeOfData;
    }

    public void setTypeOfData(String typeOfData) {
        this.typeOfData = typeOfData;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }
    
    public String getCkanDataset() {
        return ckanDataset;
    }

    public void setCkanDataset(String ckanDataset) {
        this.ckanDataset = ckanDataset;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public List<String> getColumnsLabel() {
        return columnsLabel;
    }

    public void setColumnsLabel(List<String> columnsLabel) {
        this.columnsLabel = columnsLabel;
    }

    public List<String> getColumnsData() {
        return columnsData;
    }

    public void setColumnsData(List<String> columnsData) {
        this.columnsData = columnsData;
    }

    public List<ChartLegend> getLegend() {
        return legend;
    }

    public void setLegend(List<ChartLegend> legend) {
        this.legend = legend;
    }

    public Float getWidthGraph() {
        return widthGraph;
    }

    public void setWidthGraph(Float widthGraph) {
        this.widthGraph = widthGraph;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public Boolean getMap() { return isMap; }

    public void setMap(Boolean map) { isMap = map; }

    public String getFieldOrder() { return fieldOrder; }

    public void setFieldOrder(String fieldOrder) { this.fieldOrder = fieldOrder; }

    public Integer getSortOrder() { return sortOrder; }

    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public List<String> getColumnsDescription() { return columnsDescription; }

    public void setColumnsDescription(List<String> columnsDescription) { this.columnsDescription = columnsDescription; }

    public Integer getTopRows() { return topRows; }

    public void setTopRows(Integer topRows) { this.topRows = topRows; }

    public Boolean getIsMap() {
        return isMap;
    }

    public void setIsMap(Boolean isMap) {
        this.isMap = isMap;
    }

    public NumberChart getNumberchart() {
        return numberchart;
    }

    public void setNumberchart(NumberChart numberchart) {
        this.numberchart = numberchart;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    
    public String getGroupRow() { return groupRow; }

    public void setGroupRow(String groupRow) { this.groupRow = groupRow; }

    public Integer getAxisXActivator() { return axisXActivator; }

    public void setAxisXActivator(Integer axisXActivator) { axisXActivator = axisXActivator; }
}
