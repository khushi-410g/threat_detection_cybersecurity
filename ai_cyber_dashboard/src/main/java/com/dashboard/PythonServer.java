package com.dashboard;

import java.io.File;

public class PythonServer {

    private Process process;
    private final String projectPath =
            "/home/khushigoel/Desktop/Threat Detection Private Repo /threat_detection_cybersecurity";

    public void startServer() {

        try {
            System.out.println("Starting Flask backend...");

            // Full path to venv python
            String pythonPath = projectPath + "/.venv/bin/python3";

            ProcessBuilder pb = new ProcessBuilder(
                    pythonPath,
                    projectPath + "/app/app.py"
            );

            pb.directory(new File(projectPath));
            pb.redirectErrorStream(true);
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);

            process = pb.start();

            // Wait for Flask server to start
            Thread.sleep(2000);

            System.out.println("✅ Flask backend started successfully.");

        } catch (Exception e) {
            System.out.println("❌ Failed to start Flask backend: " + e.getMessage());
        }
    }

    public void stopServer() {
        try {
            if (process != null) {
                process.destroy();
                System.out.println("🛑 Flask backend stopped.");
            }
        } catch (Exception ignored) {}
    }
}
