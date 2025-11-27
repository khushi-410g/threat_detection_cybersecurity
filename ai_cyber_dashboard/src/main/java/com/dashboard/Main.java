package com.dashboard;

import java.io.IOException;

import com.formdev.flatlaf.FlatDarculaLaf;

public class Main {

    public static void main(String[] args) {

        // 1️⃣ Start Python Flask backend automatically
        startPythonBackend();

        // 2️⃣ Launch Java GUI
        FlatDarculaLaf.setup();
        new DashboardUI().setVisible(true);
    }

    private static void startPythonBackend() {
        try {
            // Change this to your actual project path
            String pythonFile = "/home/khushigoel/Desktop/Threat Detection Private Repo /threat_detection_cybersecurity/app/app.py";

            ProcessBuilder builder = new ProcessBuilder(
                    "python3", pythonFile
            );

            builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            builder.redirectError(ProcessBuilder.Redirect.INHERIT);

            // Start Flask backend (non-blocking)
            builder.start();

            System.out.println("🔥 Flask backend started automatically.");

        } catch (IOException e) {
            System.out.println("❌ Failed to start Flask backend: " + e.getMessage());
        }
    }
}
