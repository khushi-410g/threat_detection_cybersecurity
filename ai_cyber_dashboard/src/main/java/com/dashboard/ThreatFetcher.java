package com.dashboard;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.Timer;

import org.json.JSONArray;
import org.json.JSONObject;

public class ThreatFetcher {

    private final DashboardUI ui;

    public ThreatFetcher(DashboardUI ui) {
        this.ui = ui;
    }

    // Auto-refresh every 2 sec
    public void start() {
        new Timer(2000, e -> fetchAndUpdate()).start();
    }

    private void fetchAndUpdate() {

        JSONArray arr = fetchThreatList();
        if (arr == null) return;

        for (int i = 0; i < arr.length(); i++) {

            JSONObject data = arr.getJSONObject(i);

            String threat = data.optString("threat", "normal");
            double conf    = data.optDouble("confidence", 0.0);
            String ip      = data.optString("ip", "N/A");
            String time    = data.optString("time", "N/A");

            // Update UI components
            ui.addAlert(threat, ip, conf, time);
            ui.addTableRow(ip, threat, conf, time);
            ui.updatePie(threat);
            ui.updateTimeSeries(threat);
        }
    }

    private JSONArray fetchThreatList() {

        try {
            URL url = new URL("http://127.0.0.1:5000/detect");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null)
                result.append(line);

            reader.close();

            // API returns a JSON ARRAY
            return new JSONArray(result.toString());

        } catch (Exception e) {
            System.out.println("⚠ Flask unreachable: " + e.getMessage());
            return null;
        }
    }
}
