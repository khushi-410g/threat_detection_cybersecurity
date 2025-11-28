package com.dashboard;

import java.io.File;
import java.io.IOException;

public class PythonServer {

    private Process process;

    private final String projectPath =
            "/home/khushigoel/Desktop/Threat Detection Private Repo /threat_detection_cybersecurity";

    public void startServer() {
        try {
            System.out.println("Starting Flask backend...");

            ProcessBuilder pb = new ProcessBuilder(
                projectPath + "/.venv/bin/python3",
                projectPath + "/app/app.py"
            );

            pb.directory(new File(projectPath));
            pb.redirectErrorStream(true);
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);

            process = pb.start();

            Thread.sleep(2000);

            System.out.println("✅ Flask backend started!");
        } catch (IOException | InterruptedException e) {
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
