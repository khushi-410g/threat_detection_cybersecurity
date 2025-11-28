package com.dashboard;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ThreatFetcher {

    private DashboardUI ui;

    public ThreatFetcher(DashboardUI ui) {
        this.ui = ui;
    }

    public void start() {
        new Timer(2000, e -> fetchAndUpdate()).start();
    }

    private void fetchAndUpdate() {
        JSONArray arr = fetchThreatList();
        if (arr == null) return;

        for (int i = 0; i < arr.length(); i++) {
            JSONObject data = arr.getJSONObject(i);

            String threat = data.getString("threat");
            String ip = data.getString("ip");
            double conf = data.getDouble("confidence");
            String time = data.getString("time");

            // Update UI everywhere
            ui.addAlert(threat, ip, conf);
            ui.addTableRow(ip, threat, conf, time);
            ui.updatePie(threat);
            ui.updateTimeSeries(threat);
        }
    }

    private JSONArray fetchThreatList() {
        try {
            URL url = new URL("http://127.0.0.1:5000/detect");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                result.append(line);

            return new JSONArray(result.toString());

        } catch (Exception e) {
            System.out.println("⚠ Backend unreachable: " + e.getMessage());
            return null;
        }
    }
}
