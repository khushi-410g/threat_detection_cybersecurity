package com.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class DashboardUI extends JFrame {

    JTextArea alertArea;
    JTable table;
    PieChartPanel piePanel;
    TimeSeriesPanel timePanel;

    public DashboardUI() {

        setTitle("AI-Powered Threat Dashboard");
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // NEON BACKGROUND
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(10, 10, 15));
        add(mainPanel);

        // ---- TOP CYBERPUNK HEADER ----
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(1400, 80));
        header.setBackground(new Color(15, 15, 25));

        JLabel title = new JLabel("AI-Powered Threat Dashboard", JLabel.CENTER);
        title.setFont(new Font("Orbitron", Font.BOLD, 26));
        title.setForeground(new Color(0, 255, 255));
        title.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        header.add(title, BorderLayout.CENTER);

        JButton trainBtn = new JButton("⚡ Retrain AI Model");
        trainBtn.setBackground(new Color(30, 30, 50));
        trainBtn.setForeground(Color.CYAN);
        trainBtn.setFont(new Font("Orbitron", Font.BOLD, 14));
        trainBtn.setBorder(new LineBorder(Color.CYAN, 2));
        trainBtn.setFocusPainted(false);
        trainBtn.setPreferredSize(new Dimension(180, 40));

        trainBtn.addActionListener(e -> retrainAI());

        header.add(trainBtn, BorderLayout.EAST);

        mainPanel.add(header, BorderLayout.NORTH);

        // ---- LEFT ICON SIDEBAR ----
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(90, 900));
        sidebar.setBackground(new Color(15, 15, 25));
        sidebar.setLayout(new GridLayout(6, 1, 10, 10));

        sidebar.add(iconButton("shield.png"));
        sidebar.add(iconButton("warning.png"));
        sidebar.add(iconButton("ai.png"));
        sidebar.add(iconButton("lock.png"));
        sidebar.add(iconButton("network.png"));

        mainPanel.add(sidebar, BorderLayout.WEST);

        // ---- CENTER PANEL ----
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(10, 10, 15));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // Live Alerts (NEON PANEL)
        alertArea = new JTextArea();
        alertArea.setEditable(false);
        alertArea.setBackground(new Color(20, 20, 30));
        alertArea.setForeground(Color.GREEN);
        alertArea.setFont(new Font("Consolas", Font.PLAIN, 14));

        JScrollPane alertScroll = new JScrollPane(alertArea);
        applyNeonBorder(alertScroll);
        alertScroll.setBorder(
                BorderFactory.createTitledBorder(
                        new LineBorder(Color.CYAN, 2),
                        "Live Alerts",
                        0, 0,
                        new Font("Orbitron", Font.BOLD, 14),
                        Color.CYAN));

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.30; gbc.weighty = 0.40;
        centerPanel.add(alertScroll, gbc);

        // PIE CHART PANEL
        piePanel = new PieChartPanel();
        applyNeonBorder(piePanel);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 0.35;
        centerPanel.add(piePanel, gbc);

        // TABLE PANEL (NEON)
        String[] cols = {"IP", "Threat", "Conf", "Time"};
        table = new JTable(new DefaultTableModel(cols, 0));
        table.setBackground(new Color(25, 25, 35));
        table.setForeground(Color.WHITE);
        table.setRowHeight(26);
        table.setGridColor(Color.CYAN);
        table.setFont(new Font("Consolas", Font.PLAIN, 14));

        JScrollPane tableScroll = new JScrollPane(table);
        applyNeonBorder(tableScroll);

        tableScroll.setBorder(
                BorderFactory.createTitledBorder(
                        new LineBorder(Color.MAGENTA, 2),
                        "Network Map",
                        0, 0,
                        new Font("Orbitron", Font.BOLD, 14),
                        Color.MAGENTA));

        gbc.gridx = 2; gbc.gridy = 0;
        gbc.weightx = 0.35;
        centerPanel.add(tableScroll, gbc);

        // TIME-SERIES CHART PANEL
        timePanel = new TimeSeriesPanel();
        applyNeonBorder(timePanel);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1; gbc.weighty = 0.60;
        centerPanel.add(timePanel, gbc);

        // ---- START THREAT FETCHER ----
        new ThreatFetcher(this).start();
    }

    private JButton iconButton(String iconName) {
        JButton btn = new JButton();
        btn.setIcon(new ImageIcon(getClass().getResource("/icons/" + iconName)));
        btn.setBackground(new Color(20, 20, 30));
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(Color.CYAN, 2));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBorder(new LineBorder(Color.MAGENTA, 3));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBorder(new LineBorder(Color.CYAN, 2));
            }
        });
        return btn;
    }

    private void applyNeonBorder(JComponent c) {
        c.setBorder(new LineBorder(new Color(0, 255, 255), 2));
    }

    private void retrainAI() {

        JDialog dialog = new JDialog(this, "Retraining Model...", true);
        dialog.setSize(400, 120);
        dialog.setLocationRelativeTo(this);

        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        bar.setForeground(Color.CYAN);
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
                JOptionPane.showMessageDialog(this,
                        "⚡ AI Model Retrained Successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                dialog.dispose();
                JOptionPane.showMessageDialog(this,
                        "Training Failed: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }).start();

        dialog.setVisible(true);
    }

    public void addAlert(String t, String ip, double conf, String time) {

        Color color = switch (t) {
            case "ddos" -> Color.RED;
            case "brute_force" -> Color.ORANGE;
            case "port_scan" -> Color.YELLOW;
            default -> Color.GREEN;
        };

        alertArea.setForeground(color);
        alertArea.append(time + "  [" + t + "]  " + ip + " | conf=" + conf + "\n");
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
