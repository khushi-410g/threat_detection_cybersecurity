package com.dashboard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class DashboardUI extends JFrame {

    JTextArea alertArea;
    JTable table;
    PieChartPanel piePanel;
    TimeSeriesPanel timePanel;

    public DashboardUI() {

        setTitle("AI-Powered Threat Dashboard");
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.BOTH;

        // Header
        JPanel topBar = new JPanel(new BorderLayout());
        JLabel header = new JLabel("AI-Powered Threat Dashboard", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        topBar.add(header, BorderLayout.CENTER);

        JButton trainBtn = new JButton("Retrain AI Model");
        trainBtn.setPreferredSize(new Dimension(160, 35));
        topBar.add(trainBtn, BorderLayout.EAST);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 1; gbc.weighty = 0.05;
        add(topBar, gbc);

        // Alerts
        alertArea = new JTextArea();
        alertArea.setEditable(false);
        JScrollPane alertScroll = new JScrollPane(alertArea);
        alertScroll.setBorder(BorderFactory.createTitledBorder("Live Alerts"));

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.25; gbc.weighty = 0.40;
        add(alertScroll, gbc);

        // Pie Chart
        piePanel = new PieChartPanel();
        piePanel.setBorder(BorderFactory.createTitledBorder("Attack Statistics"));
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 0.35;
        add(piePanel, gbc);

        // Table
        String[] cols = {"IP", "Threat", "Confidence", "Time"};
        table = new JTable(new DefaultTableModel(cols, 0));
        table.setRowHeight(24);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Network Map"));

        gbc.gridx = 2; gbc.gridy = 1;
        gbc.weightx = 0.40;
        add(tableScroll, gbc);

        // Time Series
        timePanel = new TimeSeriesPanel();
        timePanel.setBorder(BorderFactory.createTitledBorder("Last 60 Sec Threat Timeline"));

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.weightx = 1; gbc.weighty = 0.50;
        add(timePanel, gbc);

        new ThreatFetcher(this).start();
    }

    public void addAlert(String threat, String ip, double conf, String time) {
        alertArea.append(time + " → [" + threat + "] " + ip +
                " | conf=" + conf + "\n");
    }

    public void addTableRow(String ip, String threat, double conf, String time) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{ip, threat, conf, time});
    }

    public void updatePie(String t) {
        piePanel.increment(t);
    }

    public void updateTimeSeries(int count) {
        timePanel.updateSeries(count);
    }
}
