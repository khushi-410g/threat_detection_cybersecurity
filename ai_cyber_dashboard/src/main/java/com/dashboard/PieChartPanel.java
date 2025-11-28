package com.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class PieChartPanel extends JPanel {

    DefaultPieDataset dataset;

    public PieChartPanel() {
        dataset = new DefaultPieDataset();

        dataset.setValue("normal", 1);
        dataset.setValue("port_scan", 1);
        dataset.setValue("ddos", 1);
        dataset.setValue("brute_force", 1);

        JFreeChart chart = ChartFactory.createPieChart(
                "Attack Types",
                dataset,
                true, true, false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(50, 50, 50));
        plot.setOutlineVisible(false);

        setLayout(new BorderLayout());
        add(new ChartPanel(chart), BorderLayout.CENTER);
    }

    public void increment(String threat) {
        Number current = dataset.getValue(threat);
        dataset.setValue(threat, current.doubleValue() + 1);
    }
}
