package es.aragon.opendata.visual.dummies.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import es.aragon.opendata.visual.models.chart.ChartData;
import es.aragon.opendata.visual.models.chart.ChartDataGeneric;
import es.aragon.opendata.visual.models.chart.ChartList;

public class DummieChartGenerator {

    private DummieChartGenerator() {
    }

    public static ChartList generateDummieChartList(Integer size) {
        ChartList charts = new ChartList();

        charts.setCharts(generateCharts(size));

        return charts;
    }

    public static ChartDataGeneric generateDummieChart() {
        ChartDataGeneric chartDataGeneric = new ChartDataGeneric();

        chartDataGeneric.setType("bar");
        chartDataGeneric.setId(UUID.randomUUID().toString());
        chartDataGeneric.setLabels(generateChartLabels());
        chartDataGeneric.setData(generateChartData());

        return chartDataGeneric;
    }

    private static List<ChartDataGeneric> generateCharts(Integer size) {
        List<ChartDataGeneric> l = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            ChartDataGeneric chartDataGeneric = new ChartDataGeneric();

            chartDataGeneric.setType("bar");
            chartDataGeneric.setId(UUID.randomUUID().toString());
            chartDataGeneric.setLabels(generateChartLabels());
            chartDataGeneric.setData(generateChartData());

            l.add(chartDataGeneric);
        }

        return l;
    }

    private static List<String> generateChartLabels() {
        List<String> list = new ArrayList<>();

        list.add("2006");
        list.add("2007");
        list.add("2008");
        list.add("2009");
        list.add("2010");

        return list;
    }

    private static List<ChartData> generateChartData() {
        List<ChartData> list = new ArrayList<>();

        for (int i = 0; i < 1; i++) {
            list.add(generateData());
        }

        return list;
    }

    private static ChartData generateData() {
        ChartData c = new ChartData();

        c.setLabel("test label");
        c.setData(data());

        return c;
    }

    private static List<String> data() {
        List<String> list = new ArrayList<>();

        list.add("65");
        list.add("59");
        list.add("80");
        list.add("81");
        list.add("56");

        return list;
    }
}