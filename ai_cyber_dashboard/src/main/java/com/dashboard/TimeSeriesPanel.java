package com.dashboard;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class TimeSeriesPanel extends JPanel {

    private XYSeries ddosSeries;
    private XYSeries portScanSeries;
    private XYSeries bruteForceSeries;
    private XYSeries normalSeries;

    private int index = 0;
    private LinkedList<String> last60 = new LinkedList<>();

    public TimeSeriesPanel() {

        ddosSeries        = new XYSeries("DDoS");
        portScanSeries    = new XYSeries("Port Scan");
        bruteForceSeries  = new XYSeries("Brute Force");
        normalSeries      = new XYSeries("Normal");

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(ddosSeries);
        dataset.addSeries(portScanSeries);
        dataset.addSeries(bruteForceSeries);
        dataset.addSeries(normalSeries);

        // Create smooth XY spline chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Threat Timeline (Last 60 Events)",
                "Event #",
                "Activity Level",
                dataset
        );

        XYPlot plot = chart.getXYPlot();

        // 🔵 Smooth curved lines
        XYSplineRenderer renderer = new XYSplineRenderer();
        renderer.setPrecision(8);

        // Academic theme colors
        renderer.setSeriesPaint(0, new Color(255, 120, 120)); // ddos red
        renderer.setSeriesPaint(1, new Color(255, 200, 120)); // port scan orange
        renderer.setSeriesPaint(2, new Color(255, 80, 80));   // brute force dark red
        renderer.setSeriesPaint(3, new Color(100, 180, 100)); // normal green

        plot.setRenderer(renderer);

        // Background / Grid
        plot.setBackgroundPaint(new Color(235, 235, 235));
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setDomainGridlinePaint(Color.GRAY);

        // Y-axis always visible
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        range.setAutoRangeIncludesZero(false);

        chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 14));

        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);

        add(panel);
    }

    public void updateSeries(String threat) {

        index++;

        // maintain last 60 data points only
        last60.add(threat);
        if (last60.size() > 60)
            last60.removeFirst();

        refreshGraph();
    }

    private void refreshGraph() {

        // clear all series
        ddosSeries.clear();
        portScanSeries.clear();
        bruteForceSeries.clear();
        normalSeries.clear();

        int i = 0;
        for (String t : last60) {
            switch (t) {
                case "ddos":
                    ddosSeries.add(i, 4);
                    break;

                case "port_scan":
                    portScanSeries.add(i, 3);
                    break;

                case "brute_force":
                    bruteForceSeries.add(i, 5);
                    break;

                default:
                    normalSeries.add(i, 1);
            }
            i++;
        }
    }
}
