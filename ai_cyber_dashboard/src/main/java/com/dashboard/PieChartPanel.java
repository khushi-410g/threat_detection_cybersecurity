package com.dashboard;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class PieChartPanel extends JPanel {

    private DefaultPieDataset dataset;
    private Map<String, Integer> counter;

    public PieChartPanel() {

        counter = new HashMap<>();
        counter.put("normal", 1);
        counter.put("port_scan", 1);
        counter.put("ddos", 1);
        counter.put("brute_force", 1);

        dataset = new DefaultPieDataset();
        updateDataset();

        JFreeChart chart = ChartFactory.createPieChart(
                "Threat Distribution",
                dataset,
                true, // legend
                true,
                false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(240, 240, 240));
        plot.setOutlineVisible(false);

        // ⭐ ORIGINAL ACADEMIC COLORS
        plot.setSectionPaint("normal",      new Color(100, 180, 100));
        plot.setSectionPaint("port_scan",   new Color(255, 200, 120));
        plot.setSectionPaint("ddos",        new Color(255, 120, 120));
        plot.setSectionPaint("brute_force", new Color(255, 80, 80));

        plot.setLabelBackgroundPaint(Color.WHITE);
        plot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 12));
        plot.setSimpleLabels(true);

        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1}"));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true);
        add(chartPanel);
    }

    private void updateDataset() {
        dataset.setValue("normal", counter.get("normal"));
        dataset.setValue("port_scan", counter.get("port_scan"));
        dataset.setValue("ddos", counter.get("ddos"));
        dataset.setValue("brute_force", counter.get("brute_force"));
    }

    public void increment(String threat) {
        if (!counter.containsKey(threat)) return;

        counter.put(threat, counter.get(threat) + 1);
        updateDataset();
        repaint();
    }
}
