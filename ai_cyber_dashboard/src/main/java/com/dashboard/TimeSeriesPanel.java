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

        series = new TimeSeries("Threat Level");
        TimeSeriesCollection dataset = new TimeSeriesCollection(series);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Last 60 Sec Threat Timeline",
                "Time",
                "Threat Level",
                dataset,
                false, true, false
        );

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(new Color(25, 25, 35));
        plot.setDomainGridlinePaint(Color.CYAN);
        plot.setRangeGridlinePaint(Color.MAGENTA);

        series.setMaximumItemCount(60);

        setLayout(new BorderLayout());
        add(new ChartPanel(chart), BorderLayout.CENTER);
    }

    public void updateSeries(int v) {
        series.addOrUpdate(new Second(), v);
    }
}
