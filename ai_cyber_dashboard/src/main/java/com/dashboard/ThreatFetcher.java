package com.dashboard;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class ThreatFetcher extends Thread {

    DashboardUI ui;

    public ThreatFetcher(DashboardUI ui) {
        this.ui = ui;
    }

    private JSONArray fetchThreatList() {
        try {
            URL url = new URL("http://127.0.0.1:5000/detect");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            )) {

                String line, result = "";
                while ((line = reader.readLine()) != null) {
                    result += line;
                }

                return new JSONArray(result);   // <-- FIXED
            }

        } catch (Exception e) {
            System.out.println("ERROR fetching threats: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void run() {
        while (true) {

            JSONArray arr = fetchThreatList();
            if (arr != null) {

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

            try { sleep(2000); } catch (InterruptedException ignored) {}
        }
    }
}
