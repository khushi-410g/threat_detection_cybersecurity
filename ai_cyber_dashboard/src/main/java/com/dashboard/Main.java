package com.dashboard;

import com.formdev.flatlaf.FlatDarculaLaf;

public class Main {
    public static void main(String[] args) {

        FlatDarculaLaf.setup();  // Dark UI theme

        DashboardUI ui = new DashboardUI();
        ui.setVisible(true);

        // 🚀 START THE THREAD THAT CALLS FLASK API EVERY 2 SECONDS
        ui.startThreatFetcher();
    }
}
