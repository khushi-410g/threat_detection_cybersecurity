package com.dashboard;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class DashboardUI extends JFrame {

    JTextArea alertArea;
    JTable table;
    PieChartPanel piePanel;
    TimeSeriesPanel timePanel;

    public DashboardUI() {

        setTitle("AI-Powered Threat Dashboard");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        // HEADER
        JLabel header = new JLabel("AI-Powered Threat Dashboard", JLabel.CENTER);
        header.setFont(new Font("Verdana", Font.BOLD, 26));

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 0.1;
        add(header, gbc);

        // AI Retrain Button in Header
        JButton trainBtn = new JButton("Retrain AI Model");
        trainBtn.addActionListener(e -> showTrainingPopup());

        gbc.gridx = 2; gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(trainBtn, gbc);

        // LEFT — ALERTS
        alertArea = new JTextArea();
        alertArea.setEditable(false);
        JScrollPane alertScroll = new JScrollPane(alertArea);
        alertScroll.setBorder(BorderFactory.createTitledBorder("Live Alerts"));

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        gbc.weighty = 0.4;
        add(alertScroll, gbc);

        // CENTER — PIE CHART
        piePanel = new PieChartPanel();
        piePanel.setPreferredSize(new Dimension(350, 350));
        piePanel.setBorder(BorderFactory.createTitledBorder("Attack Statistics"));

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 0.4;
        add(piePanel, gbc);

        // RIGHT — TABLE
        String[] cols = {"IP", "Threat", "Confidence", "Time"};
        table = new JTable(new DefaultTableModel(cols, 0));
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Network Map"));

        gbc.gridx = 2; gbc.gridy = 1;
        gbc.weightx = 0.3;
        add(tableScroll, gbc);

        // BOTTOM — TIME SERIES PANEL
        timePanel = new TimeSeriesPanel();
        timePanel.setBorder(BorderFactory.createTitledBorder("Last 60 sec Threat Timeline"));

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        gbc.weighty = 0.5;
        add(timePanel, gbc);

        // START FETCHER
        new ThreatFetcher(this).start();
    }

    // ALERT BLINK EFFECT
    private void blinkAlert() {
        new Thread(() -> {
            try {
                for (int i = 0; i < 4; i++) {
                    alertArea.setVisible(false);
                    Thread.sleep(200);
                    alertArea.setVisible(true);
                    Thread.sleep(200);
                }
            } catch (Exception ignored) {}
        }).start();
    }

    // COLORED ALERTS
    public void addAlert(String threat, String ip, double conf, String time) {
        String msg = "[" + threat + "] " + ip + " | Conf=" + conf + " | " + time;

        switch (threat) {
            case "ddos":
            case "brute_force":
                alertArea.setForeground(Color.RED);
                blinkAlert();
                break;
            case "port_scan":
                alertArea.setForeground(Color.ORANGE);
                break;
            default:
                alertArea.setForeground(Color.GREEN);
        }

        alertArea.append(msg + "\n");
    }

    // COLORED TABLE ROWS
    public void addTableRow(String ip, String threat, double conf, String time) {

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{ip, threat, conf, time});

        table.setDefaultRenderer(Object.class, (tbl, value, isSelected, hasFocus, row, col) -> {
            Component c = new DefaultTableCellRenderer()
                    .getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);

            String t = (String) tbl.getValueAt(row, 1);

            if (t.equals("ddos") || t.equals("brute_force"))
                c.setBackground(new Color(255, 120, 120));
            else if (t.equals("port_scan"))
                c.setBackground(new Color(255, 200, 120));
            else
                c.setBackground(new Color(200, 255, 200));

            return c;
        });
    }

    // PIE CHART UPDATE
    public void updatePie(String threat) {
        piePanel.increment(threat);
    }

    // TIME SERIES UPDATE
    public void updateTimeSeries(String threat) {
        timePanel.updateSeries(threat);
    }

    // FAKE AI TRAINING POPUP
    private void showTrainingPopup() {
        JDialog dialog = new JDialog(this, "AI Model Training", true);

        JProgressBar bar = new JProgressBar();
        bar.setMinimum(0);
        bar.setMaximum(100);
        bar.setValue(0);

        dialog.add(bar);
        dialog.setSize(300, 80);
        dialog.setLocationRelativeTo(this);

        new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                bar.setValue(i);
                try { Thread.sleep(30); } catch (Exception ignored) {}
            }
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "AI Retraining Complete!");
        }).start();

        dialog.setVisible(true);
    }
}
