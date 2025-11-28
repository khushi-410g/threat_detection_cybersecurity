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

        // WINDOW
        setTitle("AI-Powered Threat Monitoring Dashboard");
        setSize(1250, 780);
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Main Layout
        setLayout(new BorderLayout());

        // ------------------------------
        // 🔵 HEADER BAR
        // ------------------------------
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 30, 30));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("AI-Powered Threat Monitoring Dashboard", JLabel.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        JButton retrainBtn = new JButton("Retrain AI Model");
        retrainBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        retrainBtn.addActionListener(e -> showTrainingPopup());

        header.add(title, BorderLayout.WEST);
        header.add(retrainBtn, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ------------------------------
        // 🔵 CENTER PANEL — ALERTS, PIE, TABLE
        // ------------------------------
        JPanel center = new JPanel(new GridLayout(1, 3, 10, 10));
        center.setBackground(new Color(40, 40, 40));
        center.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // LEFT — ALERT AREA
        alertArea = new JTextArea();
        alertArea.setEditable(false);
        alertArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        alertArea.setBackground(new Color(20, 20, 20));
        alertArea.setForeground(Color.WHITE);

        JScrollPane alertScroll = new JScrollPane(alertArea);
        alertScroll.setBorder(BorderFactory.createTitledBorder("Live Alerts"));

        center.add(alertScroll);

        // CENTER — PIE CHART
        piePanel = new PieChartPanel();
        piePanel.setBorder(BorderFactory.createTitledBorder("Attack Distribution"));

        center.add(piePanel);

        // RIGHT — TABLE
        String[] cols = { "Source IP", "Threat", "Confidence", "Timestamp" };
        table = new JTable(new DefaultTableModel(cols, 0));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(26);

        table.getColumnModel().getColumn(3).setPreferredWidth(180); // full time visible

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Network Events"));

        center.add(tableScroll);

        add(center, BorderLayout.CENTER);

        // ------------------------------
        // 🔵 BOTTOM — TIME SERIES GRAPH
        // ------------------------------
        timePanel = new TimeSeriesPanel();
        timePanel.setBorder(BorderFactory.createTitledBorder("Threat Timeline (last 60 events)"));

        add(timePanel, BorderLayout.SOUTH);

        // ------------------------------
        // START FETCHER
        // ------------------------------
        new ThreatFetcher(this).start();
    }

    // -------------------------------------------
    // 🔵 ALERTS
    // -------------------------------------------
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

    public void addAlert(String threat, String ip, double conf, String time) {
        String msg = "[" + threat.toUpperCase() + "] " + ip + " | conf=" + conf + " | " + time;

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

    // -------------------------------------------
    // 🔵 TABLE ROW COLORS
    // -------------------------------------------
    public void addTableRow(String ip, String threat, double conf, String time) {

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{ip, threat, conf, time});

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);

                String t = (String) tbl.getValueAt(row, 1);

                if (t.equals("ddos") || t.equals("brute_force")) {
                    c.setBackground(new Color(255, 120, 120));
                } else if (t.equals("port_scan")) {
                    c.setBackground(new Color(255, 200, 120));
                } else {
                    c.setBackground(new Color(200, 255, 200));
                }

                if (isSelected)
                    c.setBackground(new Color(150, 150, 255));

                return c;
            }
        });
    }

    // -------------------------------------------
    // 🔵 PIE + TIME SERIES UPDATE
    // -------------------------------------------
    public void updatePie(String threat) {
        piePanel.increment(threat);
    }

    public void updateTimeSeries(String threat) {
        timePanel.updateSeries(threat);
    }

    // -------------------------------------------
    // 🔵 TRAINING POPUP
    // -------------------------------------------
    private void showTrainingPopup() {
        JDialog dialog = new JDialog(this, "Retraining AI Model...", true);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(0);

        dialog.add(bar);
        dialog.setSize(350, 90);
        dialog.setLocationRelativeTo(this);

        // simulate training
        new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                bar.setValue(i);
                try { Thread.sleep(30); } catch (Exception ignored) {}
            }
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Model Retraining Complete!");
        }).start();

        dialog.setVisible(true);
    }
}
