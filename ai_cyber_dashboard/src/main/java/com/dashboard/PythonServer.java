package com.dashboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PythonServer {

    private Process process;

    // Path to Flask app
    private static final String PYTHON_SCRIPT =
            "app/app.py";   // relative to project root

    // Activate VENV
    private static final String VENV_PYTHON =
            "../.venv/bin/python3";   // Linux virtual environment

    public void startServer() {

        // Check if Flask is already running
        if (isFlaskRunning()) {
            System.out.println("✔ Flask server already running.");
            return;
        }

        try {
            System.out.println("Starting Flask backend...");

            ProcessBuilder builder = new ProcessBuilder(
                    VENV_PYTHON, PYTHON_SCRIPT
            );

            builder.redirectErrorStream(true);

            process = builder.start();

            // Background thread to print Flask logs
            new Thread(() -> {
                try (BufferedReader reader =
                             new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("FLASK > " + line);
                    }
                } catch (IOException ignored) {}
            }).start();

            System.out.println("✔ Flask backend started!");

        } catch (Exception e) {
            System.out.println("❌ Failed to start Flask: " + e.getMessage());
        }
    }

    private boolean isFlaskRunning() {
        try {
            Process check = new ProcessBuilder("bash", "-c",
                    "lsof -i :5000 | grep LISTEN").start();

            BufferedReader br = new BufferedReader(new InputStreamReader(check.getInputStream()));
            return br.readLine() != null;

        } catch (Exception e) {
            return false;
        }
    }

    public void stopServer() {
        try {
            if (process != null) {
                process.destroy();
                System.out.println("✔ Flask backend stopped.");
            }
        } catch (Exception ignored) {}
    }
}
