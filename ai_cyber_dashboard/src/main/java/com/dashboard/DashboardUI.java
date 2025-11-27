package com.dashboard;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
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

    public DashboardUI() {
        setTitle("AI-Powered Threat Dashboard");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JLabel header = new JLabel("AI-Powered Threat Dashboard", JLabel.CENTER);
        header.setFont(new Font("Verdana", Font.BOLD, 26));
        add(header, BorderLayout.NORTH);

        // LEFT SIDE: Alerts + Pie Chart
        JPanel leftPanel = new JPanel(new BorderLayout());

        alertArea = new JTextArea();
        alertArea.setEditable(false);
        JScrollPane alertScroll = new JScrollPane(alertArea);
        alertScroll.setBorder(BorderFactory.createTitledBorder("Live Alerts"));

        piePanel = new PieChartPanel();
        piePanel.setBorder(BorderFactory.createTitledBorder("Attack Statistics"));

        leftPanel.add(alertScroll, BorderLayout.CENTER);
        leftPanel.add(piePanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);

        // RIGHT SIDE: Network Map Table
        String[] cols = {"IP", "Threat", "Confidence", "Time"};
        table = new JTable(new DefaultTableModel(cols, 0));
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Network Map"));

        add(tableScroll, BorderLayout.CENTER);
    }

    public void startThreatFetcher() {
        // Start fetching threats
        new ThreatFetcher(this).start();
    }

    public void addAlert(String text) {
        alertArea.append(text + "\n");
    }

    public void addTableRow(String ip, String threat, double conf, String time) {
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.addRow(new Object[] { ip, threat, conf, time });
 }


    public void updatePie(String threat) {
        piePanel.increment(threat);
    }
}
