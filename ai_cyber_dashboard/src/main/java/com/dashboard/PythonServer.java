package com.dashboard;

import java.io.File;
import java.io.IOException;

public class PythonServer {

    private Process process;

    public void startServer() {
        try {
            ProcessBuilder pb = new ProcessBuilder("python3", "app/app.py");
            pb.directory(new File("/home/khushigoel/Desktop/Threat Detection Private Repo /threat_detection_cybersecurity"));
            pb.redirectErrorStream(true);
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            process = pb.start();
            System.out.println("Flask started.");
        } catch (IOException e) {
            System.out.println("Error starting Flask: " + e.getMessage());
        }
    }
}
