package com.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
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


    public void addAlert(String threat, String ip, double conf) {
    String msg = "[" + threat + "] from " + ip + " | Conf=" + conf;

    if (threat.equals("ddos") || threat.equals("brute_force")) {
        // High severity → blinking red
        alertArea.setForeground(Color.RED);
        blinkAlert();
    } else if (threat.equals("port_scan")) {
        alertArea.setForeground(Color.ORANGE);
    } else {
        alertArea.setForeground(Color.GREEN);
    }

    alertArea.append(msg + "\n");
   }


   public void addTableRow(String ip, String threat, double conf, String time) {

    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.addRow(new Object[]{ip, threat, conf, time});

    table.setDefaultRenderer(Object.class, (tbl, value, isSelected, hasFocus, row, col) -> {
        Component c = new DefaultTableCellRenderer().getTableCellRendererComponent(
                tbl, value, isSelected, hasFocus, row, col
        );

        String t = (String) tbl.getValueAt(row, 1); // threat column

        if (t.equals("ddos") || t.equals("brute_force"))
            c.setBackground(new Color(255, 80, 80));   // red
        else if (t.equals("port_scan"))
            c.setBackground(new Color(255, 180, 80)); // orange
        else
            c.setBackground(new Color(120, 255, 120)); // green

        return c;
        });
    }



    public void updatePie(String threat) {
        piePanel.increment(threat);
    }
}
