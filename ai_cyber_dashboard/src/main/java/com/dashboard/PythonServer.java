package com.dashboard;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PythonServer {

    private Process process;

    public void startServer() {
        try {
            // Path to your project
            String projectPath = "/home/khushigoel/Desktop/Threat Detection Private Repo /threat_detection_cybersecurity";

            // Path to Python inside venv
            String pythonPath = projectPath + "/.venv/bin/python3";

            // Python app to run
            String pythonApp = projectPath + "/app/app.py";

            ProcessBuilder pb = new ProcessBuilder(
                pythonPath, pythonApp
            );

            pb.redirectErrorStream(true);

            process = pb.start();

            // Read the Flask output
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream())
                )) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("[FLASK] " + line);
                    }
                } catch (Exception ignored) {}
            }).start();

            System.out.println("🔥 Python Flask server started.");

        } catch (Exception e) {
            System.out.println("❌ Failed to start Python backend: " + e.getMessage());
        }
    }

    public void stopServer() {
        if (process != null) {
            process.destroy();
            System.out.println("🛑 Python server stopped.");
        }
    }
}
