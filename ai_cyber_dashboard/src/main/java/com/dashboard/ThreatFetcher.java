package com.dashboard;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.Timer;

import org.json.JSONObject;

public class ThreatFetcher {

    private final DashboardUI ui;

    public ThreatFetcher(DashboardUI ui) {
        this.ui = ui;
    }

    public void start() {
        new Timer(2000, e -> fetchAndUpdate()).start();
    }

    private void fetchAndUpdate() {
        JSONObject obj = fetchThreat();
        if (obj == null) return;

        String ip = obj.getString("ip");
        String threat = obj.getString("threat");
        double conf = obj.getDouble("confidence");
        String time = obj.getString("time");

        ui.addAlert(threat, ip, conf, time);
        ui.addTableRow(ip, threat, conf, time);
        ui.updatePie(threat);

        // Severity-based graph spike
        int value = switch (threat) {
            case "port_scan" -> 1;
            case "brute_force" -> 2;
            case "ddos" -> 3;
            default -> 0;
        };

        ui.updateTimeSeries(value);
    }

    private JSONObject fetchThreat() {
        try {
            URL url = new URL("http://127.0.0.1:5000/detect");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(2000);
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            );

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null)
                sb.append(line);

            return new JSONObject(sb.toString());

        } catch (Exception e) {
            System.out.println("⚠ Flask unreachable: " + e.getMessage());
            return null;
        }
    }
}
