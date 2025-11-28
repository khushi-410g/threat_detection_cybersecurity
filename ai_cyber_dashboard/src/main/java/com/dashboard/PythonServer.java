package com.dashboard;

import java.io.File;
import java.io.IOException;

public class PythonServer {

    private Process process;

    public void startServer() {
        try {
            String projectPath = "/home/khushigoel/Desktop/Threat Detection Private Repo /threat_detection_cybersecurity";
            String pythonFile = projectPath + "/app/app.py";

            ProcessBuilder pb = new ProcessBuilder("python3", pythonFile);
            pb.directory(new File(projectPath));

            pb.redirectErrorStream(true);
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);

            process = pb.start();
            System.out.println("🔥 Flask backend started.");

        } catch (IOException e) {
            System.out.println("❌ Failed to start Flask: " + e.getMessage());
        }
    }

    public void stopServer() {
        if (process != null && process.isAlive()) {
            process.destroy();
            System.out.println("🛑 Flask backend stopped.");
        }
    }
}
