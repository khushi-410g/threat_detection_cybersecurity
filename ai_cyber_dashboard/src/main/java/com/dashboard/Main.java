package com.dashboard;

import com.formdev.flatlaf.FlatDarculaLaf;

public class Main {
    public static void main(String[] args) {
        FlatDarculaLaf.setup();

        DashboardUI ui = new DashboardUI();
        ui.setVisible(true);
    }
}
