package com.dashboard;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class ThreatFetcher extends Thread {

    DashboardUI ui;

    public ThreatFetcher(DashboardUI ui) {
        this.ui = ui;
    }

    private JSONObject getThreat() {
        try {
            URL url = new URL("http://localhost:5000/detect");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            )) {

                String line, result = "";
                while ((line = reader.readLine()) != null) {
                    result += line;
                }

                return new JSONObject(result);
            }
        } catch (java.io.IOException | org.json.JSONException e) {
            return null;
        }
    }

    @Override
    public void run() {
        while (true) {
            JSONObject data = getThreat();
            if (data != null) {
                String threat = data.getString("threat");
                double conf = data.getDouble("confidence");
                String ip = data.getString("ip");

                ui.addAlert("[" + threat + "] from " + ip + " | Conf=" + conf);
                ui.addTableRow(ip, threat, conf);
                ui.updatePie(threat);
            }

            try { Thread.sleep(2000); } catch (Exception ignored) {}
        }
    }
}
