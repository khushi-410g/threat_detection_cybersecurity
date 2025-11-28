package com.dashboard;

import com.formdev.flatlaf.FlatDarculaLaf;

public class Main {
    public static void main(String[] args) {

        // Modern Dark Theme
        FlatDarculaLaf.setup();

        // Auto-start Python backend
        PythonServer server = new PythonServer();
        server.startServer();

        // Launch UI
        DashboardUI ui = new DashboardUI();
        ui.setVisible(true);

        // Stop Python when closing app
        Runtime.getRuntime().addShutdownHook(new Thread(server::stopServer));
    }
}
