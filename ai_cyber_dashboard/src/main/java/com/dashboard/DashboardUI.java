package com.dashboard;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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

        // Top Bar
        JPanel top = new JPanel(new BorderLayout());
        JLabel header = new JLabel("AI-Powered Threat Dashboard", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        top.add(header, BorderLayout.CENTER);

        JButton trainBtn = new JButton("Retrain AI Model");
        trainBtn.setPreferredSize(new Dimension(160, 38));
        top.add(trainBtn, BorderLayout.EAST);

        // AI Retrain action
        trainBtn.addActionListener(e -> retrainAI());

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 1; gbc.weighty = 0.05;
        add(top, gbc);

        // Alerts Panel
        alertArea = new JTextArea();
        alertArea.setEditable(false);
        JScrollPane alertScroll = new JScrollPane(alertArea);
        alertScroll.setBorder(BorderFactory.createTitledBorder("Live Alerts"));

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.25; gbc.weighty = 0.40;
        add(alertScroll, gbc);

        // Pie Panel
        piePanel = new PieChartPanel();
        piePanel.setBorder(BorderFactory.createTitledBorder("Attack Statistics"));

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 0.35;
        add(piePanel, gbc);

        // Network Table
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

        // Start Live Fetch
        new ThreatFetcher(this).start();
    }

    private void retrainAI() {
        JDialog dialog = new JDialog(this, "Retraining Model...", true);
        dialog.setSize(400, 120);
        dialog.setLocationRelativeTo(this);

        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        dialog.add(bar);

        new Thread(() -> {
            try {
                ProcessBuilder pb = new ProcessBuilder(
                    "/home/khushigoel/Desktop/Threat Detection Private Repo /threat_detection_cybersecurity/.venv/bin/python3",
                    "/home/khushigoel/Desktop/Threat Detection Private Repo /threat_detection_cybersecurity/train_model.py"
                );

                pb.redirectErrorStream(true);
                pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                pb.start().waitFor();

                dialog.dispose();
                JOptionPane.showMessageDialog(this, "AI Model retrained successfully!");

            } catch (Exception ex) {
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Training failed: " + ex.getMessage());
            }
        }).start();

        dialog.setVisible(true);
    }

    public void addAlert(String t, String ip, double conf, String time) {
        alertArea.append(time + "  [" + t + "] " + ip + " | conf=" + conf + "\n");
    }

    public void addTableRow(String ip, String t, double conf, String time) {
        DefaultTableModel m = (DefaultTableModel) table.getModel();
        m.addRow(new Object[]{ip, t, conf, time});
    }

    public void updatePie(String t) {
        piePanel.increment(t);
    }

    public void updateTimeSeries(int value) {
        timePanel.updateSeries(value);
    }
}
