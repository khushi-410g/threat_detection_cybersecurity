package com.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class TimeSeriesPanel extends JPanel {

    private TimeSeries series;

    public TimeSeriesPanel() {
        series = new TimeSeries("Threats");

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Last 60 sec Threat Timeline",
                "Time",
                "Threats",
                new TimeSeriesCollection(series),
                false,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.DARK_GRAY);

        setLayout(new BorderLayout());
        add(new ChartPanel(chart), BorderLayout.CENTER);
    }

    public void updateSeries(String threat) {
        series.addOrUpdate(new Second(), 1);
    }
}
