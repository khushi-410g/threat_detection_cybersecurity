package com.dashboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;

import javax.swing.JPanel;

public class PieChartPanel extends JPanel {

    private HashMap<String, Integer> attackCounts;

    public PieChartPanel() {
        attackCounts = new HashMap<>();
        setPreferredSize(new Dimension(450, 300));
        setBackground(new Color(45, 45, 45));  // soft academic dark
    }

    public void updateCount(String attackType) {
        attackCounts.put(attackType, attackCounts.getOrDefault(attackType, 0) + 1);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPieChart((Graphics2D) g);
    }

    private void drawPieChart(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int total = attackCounts.values().stream().mapToInt(Integer::intValue).sum();
        if (total == 0)
            return;

        // --- Academic theme colors (matching original screenshot) ---
        Color normal = new Color(255, 91, 91);     // red
        Color portScan = new Color(66, 135, 245);  // blue
        Color ddos = new Color(255, 235, 59);      // yellow
        Color brute = new Color(76, 175, 80);      // green

        HashMap<String, Color> colorMap = new HashMap<>();
        colorMap.put("normal", normal);
        colorMap.put("port_scan", portScan);
        colorMap.put("ddos", ddos);
        colorMap.put("brute_force", brute);

        int x = 30, y = 20, width = 270, height = 270;

        int startAngle = 0;
        for (String key : attackCounts.keySet()) {
            int value = attackCounts.get(key);
            int angle = (int) Math.round((value * 360.0) / total);

            g.setColor(colorMap.getOrDefault(key, Color.GRAY));
            g.fillArc(x, y, width, height, startAngle, angle);

            startAngle += angle;
        }

        // Draw labels
        int legendX = 330;
        int legendY = 60;

        g.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        g.setColor(Color.WHITE);

        for (String key : attackCounts.keySet()) {
            g.setColor(colorMap.getOrDefault(key, Color.GRAY));
            g.fillRect(legendX, legendY, 18, 18);

            g.setColor(Color.WHITE);
            g.drawString(key + " (" + attackCounts.get(key) + ")", legendX + 28, legendY + 15);

            legendY += 30;
        }

        // Title
        g.setColor(Color.WHITE);
        g.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g.drawString("Attack Types", 150, 15);
    }
}
