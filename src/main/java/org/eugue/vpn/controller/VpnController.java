package org.eugue.vpn.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vpn")
public class VpnController{

    private static final Logger logger = LoggerFactory.getLogger(VpnController.class);
    
    @PostMapping("/start")
    public String StartVpn() {
        logger.info("Start VPN Message Received");
        try {
            logger.info("Attempting to start vpn server...");
            
            // Execute the command
            Process process = new ProcessBuilder("sudo", "systemctl", "start", "openvpn@server")
                .redirectErrorStream(true) // Combine stdout and stderr
                .start();
            logger.info("VPN server process started in background");
            return "VPN server started in the background";
        } catch (IOException e) {
            return "Error starting VPN: " + e.getMessage();
        }
    }

    @PostMapping("/stop")
    public String StopVpn() {
        try {
            logger.info("Attempting to stop vpn server...");
            //stop vpn here
            String command = "sudo systemctl stop openvpn@server";
            Process process = new ProcessBuilder(command.split(" ")).start();
            process.waitFor();
            logger.info("VPN Server Stopped");
            return "VPN Server Stopped";
        } catch (IOException | InterruptedException e) {
            logger.info("Error stopping vpn: " + e.getMessage());
            return "Error stopping vpn: " + e.getMessage();
        }
    }

    @GetMapping("/status")
    public String CheckVpnStatus() {
        StringBuilder output = new StringBuilder();
        try {
            logger.info("Checking vpn server status...");
            ProcessBuilder builder = new ProcessBuilder("sudo", "systemctl", "status", "openvpn@server");
            Process process = builder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n"); // Accumulate output
                }
            }
            int exitValue = process.exitValue();
            output.append("Exit Value: ").append(exitValue).append("\n");
        } catch (IOException e) {
            return "Error checking VPN status: " + e.getMessage();
        }
        logger.info("VPN Status: " + output.toString());
        return output.toString();
    }

}
 
