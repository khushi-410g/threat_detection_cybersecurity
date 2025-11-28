package com.dashboard;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.Timer;

import org.json.JSONArray;
import org.json.JSONObject;

public class ThreatFetcher {

    private DashboardUI ui;

    public ThreatFetcher(DashboardUI ui) {
        this.ui = ui;
    }

    // START AUTO-TIMER
    public void start() {
        new Timer(2000, e -> fetchAndUpdate()).start();
    }

    // FETCH + UPDATE UI
    private void fetchAndUpdate() {

        JSONArray arr = fetchThreatList();

        if (arr == null) return;

        for (int i = 0; i < arr.length(); i++) {
            JSONObject data = arr.getJSONObject(i);

            String threat = data.getString("threat");
            double conf = data.getDouble("confidence");
            String ip = data.getString("ip");
            String time = data.getString("time");

            ui.addAlert("[" + threat + "] from " + ip + " | Conf=" + conf);
            ui.addTableRow(ip, threat, conf, time);
            ui.updatePie(threat);
        }
    }

    // FETCH JSON ARRAY FROM FLASK API
    private JSONArray fetchThreatList() {
        try {
            URL url = new URL("http://127.0.0.1:5000/detect");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(1500);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null)
                result.append(line);

            return new JSONArray(result.toString());

        } catch (Exception e) {
            System.out.println("⚠️ Backend unreachable: " + e.getMessage());
            return null;
        }
    }
}
