package com.dashboard;

import com.formdev.flatlaf.FlatDarculaLaf;

public class Main {

    public static void main(String[] args) {

        FlatDarculaLaf.setup();

        // Start Flask Backend Automatically
        PythonServer server = new PythonServer();
        server.startServer();

        // Launch Dashboard UI
        DashboardUI ui = new DashboardUI();
        ui.setVisible(true);

        // Stop Flask when UI closes
        Runtime.getRuntime().addShutdownHook(new Thread(server::stopServer));
    }
}
