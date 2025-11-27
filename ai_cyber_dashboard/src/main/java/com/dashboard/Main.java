package com.dashboard;

import com.formdev.flatlaf.FlatDarculaLaf;

public class Main {

    public static void main(String[] args) {

        FlatDarculaLaf.setup();

        // 1️⃣ Start Python Flask server
        PythonServer server = new PythonServer();
        server.startServer();

        // 2️⃣ Start Dashboard UI
        DashboardUI ui = new DashboardUI();
        ui.setVisible(true);
        ui.startThreatFetcher();

        // Optional: stop Python when Java closes
        Runtime.getRuntime().addShutdownHook(new Thread(server::stopServer));
    }
}
