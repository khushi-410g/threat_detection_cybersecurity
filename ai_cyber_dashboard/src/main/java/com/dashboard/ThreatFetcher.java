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

    public void start() {
        new Timer(2000, e -> fetchAndUpdate()).start();
    }

    private void fetchAndUpdate() {
        JSONArray arr = fetchThreatList();
        if (arr == null) return;

        for (int i = 0; i < arr.length(); i++) {
            JSONObject d = arr.getJSONObject(i);

            ui.addAlert(
                    d.getString("threat"),
                    d.getString("ip"),
                    d.getDouble("confidence"),
                    d.getString("time")
            );

            ui.addTableRow(
                    d.getString("ip"),
                    d.getString("threat"),
                    d.getDouble("confidence"),
                    d.getString("time")
            );

            ui.updatePie(d.getString("threat"));
            ui.updateTimeSeries(d.getString("threat"));
        }
    }

    private JSONArray fetchThreatList() {
        try {
            URL url = new URL("http://127.0.0.1:5000/detect");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1500);

            BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = r.readLine()) != null)
                sb.append(line);

            return new JSONArray(sb.toString());

        } catch (Exception e) {
            System.out.println("⚠ Backend unreachable: " + e.getMessage());
            return null;
        }
    }
}
