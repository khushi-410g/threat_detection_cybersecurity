package com.dashboard;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class PieChartPanel extends JPanel {

    DefaultPieDataset<String> dataset;

    public PieChartPanel() {
        dataset = new DefaultPieDataset<>();
        dataset.setValue("normal", 1);
        dataset.setValue("port_scan", 1);
        dataset.setValue("ddos", 1);
        dataset.setValue("brute_force", 1);

        JFreeChart chart = ChartFactory.createPieChart(
            "Attack Types",
            dataset,
            true,
            true,
            false
        );

        @SuppressWarnings("unchecked")
        PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
        plot.setSectionOutlinesVisible(false);

        setLayout(new BorderLayout());
        add(new ChartPanel(chart), BorderLayout.CENTER);
    }

    public void increment(String threat) {
        Number current = dataset.getValue(threat);
        dataset.setValue(threat, current.intValue() + 1);
    }
}
