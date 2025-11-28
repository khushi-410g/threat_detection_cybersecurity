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

    private final TimeSeries series;

    public TimeSeriesPanel() {
        series = new TimeSeries("Threats per second");

        TimeSeriesCollection dataset = new TimeSeriesCollection(series);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Last 60 Sec Threat Timeline",
                "Time",
                "Threat Count",
                dataset,
                false, true, false
        );

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(new Color(40, 40, 40));
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        setLayout(new BorderLayout());
        add(new ChartPanel(chart), BorderLayout.CENTER);

        series.setMaximumItemCount(60);
    }

    public void updateSeries(int threats) {
        series.addOrUpdate(new Second(), threats);
    }
}
