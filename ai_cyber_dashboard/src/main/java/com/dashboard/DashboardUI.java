package com.dashboard;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;

public class DashboardUI extends JFrame {

    private JTextArea alertArea;
    private JTable networkTable;
    private PieChartPanel pieChart;
    private TimeSeriesPanel timeSeriesPanel;

    public DashboardUI() {
        setTitle("AI-Powered Threat Dashboard");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Academic Theme Colors
        Color bg = new Color(30, 30, 30);
        Color panelBorder = new Color(180, 180, 180);
        Color panelBG = new Color(40, 40, 40);
        Color headerColor = new Color(220, 220, 220);

        setLayout(new BorderLayout());
        getContentPane().setBackground(bg);

        // Header
        JLabel header = new JLabel("AI-Powered Threat Dashboard", SwingConstants.CENTER);
        header.setForeground(headerColor);
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setBorder(new EmptyBorder(15, 10, 15, 10));
        add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        centerPanel.setBackground(bg);

        // Left — Alerts
        JPanel alertPanel = createPanel("Live Alerts", panelBG, panelBorder);
        alertArea = new JTextArea();
        alertArea.setEditable(false);
        alertArea.setBackground(panelBG);
        alertArea.setForeground(Color.WHITE);
        alertArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        alertPanel.add(new JScrollPane(alertArea));
        centerPanel.add(alertPanel);

        // Middle — Pie Chart
        JPanel chartPanel = createPanel("Attack Statistics", panelBG, panelBorder);
        pieChart = new PieChartPanel();
        chartPanel.add(pieChart);
        centerPanel.add(chartPanel);

        // Right — Network Map
        JPanel networkPanel = createPanel("Network Map", panelBG, panelBorder);

        String[] columns = {"IP", "Threat", "Confidence", "Time"};
        networkTable = new JTable(new DefaultTableModel(columns, 0));

        networkTable.setBackground(panelBG);
        networkTable.setForeground(Color.WHITE);
        networkTable.setGridColor(Color.GRAY);

        // Column Width Fix for Timestamp
        networkTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        networkTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        networkTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        networkTable.getColumnModel().getColumn(3).setPreferredWidth(180);

        JScrollPane tableScroll = new JScrollPane(networkTable);
        networkPanel.add(tableScroll);
        centerPanel.add(networkPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom — Timeline
        JPanel bottomPanel = createPanel("Last 60 Sec Threat Timeline", panelBG, panelBorder);
        timeSeriesPanel = new TimeSeriesPanel();
        bottomPanel.add(timeSeriesPanel);
        add(bottomPanel, BorderLayout.SOUTH);

        // Top-right — Retrain button
        JButton retrainBtn = iconButton("ai.png", "Retrain AI Model");
        retrainBtn.addActionListener(e -> PythonServer.runTraining());
        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRight.setBackground(bg);
        topRight.add(retrainBtn);
        add(topRight, BorderLayout.NORTH);
    }

    /** Smaller reusable panel builder */
    private JPanel createPanel(String title, Color bg, Color borderColor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(bg);
        panel.setBorder(new TitledBorder(new LineBorder(borderColor, 1), title,
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13), borderColor));
        return panel;
    }

    /** ICON FIXED — Auto resize + correct resource path */
    private JButton iconButton(String iconName, String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        try {
            URL iconURL = getClass().getClassLoader().getResource("icons/" + iconName);
            if (iconURL != null) {
                Image img = new ImageIcon(iconURL).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            System.out.println("Icon load failed: " + iconName);
        }

        return btn;
    }

    // Update UI from threat data
    public void addAlert(String text) {
        alertArea.append(text + "\n");
    }

    public void addTableRow(String ip, String threat, double conf, String time) {
        ((DefaultTableModel) networkTable.getModel())
                .addRow(new Object[]{ip, threat, conf, time});
    }

    public PieChartPanel getPieChartPanel() {
        return pieChart;
    }

    public TimeSeriesPanel getTimeSeriesPanel() {
        return timeSeriesPanel;
    }
}
