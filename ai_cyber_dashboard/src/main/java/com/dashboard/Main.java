package com.dashboard;

import com.formdev.flatlaf.FlatDarculaLaf;

public class Main {
    public static void main(String[] args) {
        FlatDarculaLaf.setup();  // Dark UI
        new DashboardUI().setVisible(true);
    }
}
