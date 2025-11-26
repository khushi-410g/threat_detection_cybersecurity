package com.dashboard;

import javax.swing.*;
import java.awt.*;

public class PieChartPanel extends JPanel {

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        ThreatFetcher fetcher = new ThreatFetcher();

        int malware = fetcher.getMalwareCount();
        int phishing = fetcher.getPhishingCount();
        int ransomware = fetcher.getRansomwareCount();

        int total = malware + phishing + ransomware;
        int startAngle = 0;

        // Malware slice
        int angle = (int) ((malware * 360.0) / total);
        g.setColor(Color.RED);
        g.fillArc(50, 50, 300, 300, startAngle, angle);
        startAngle += angle;

        // Phishing slice
        angle = (int) ((phishing * 360.0) / total);
        g.setColor(Color.BLUE);
        g.fillArc(50, 50, 300, 300, startAngle, angle);
        startAngle += angle;

        // Ransomware slice
        angle = (int) ((ransomware * 360.0) / total);
        g.setColor(Color.GREEN);
        g.fillArc(50, 50, 300, 300, startAngle, angle);
    }
}
