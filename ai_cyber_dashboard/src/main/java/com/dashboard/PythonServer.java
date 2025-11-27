package com.dashboard;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class PythonServer {

    private Process process;

    public void startServer() {
        try {
            String projectPath = "/home/khushigoel/Desktop/Threat Detection Private Repo /threat_detection_cybersecurity";
            String pythonPath = projectPath + "/.venv/bin/python3";
            String pythonApp = projectPath + "/app/app.py";

            ProcessBuilder pb = new ProcessBuilder(pythonPath, pythonApp);

            // ⭐ Set working directory so Flask can find model/ folder
            pb.directory(new File(projectPath));

            pb.redirectErrorStream(true);
            process = pb.start();

            // Thread to print Flask output
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
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
