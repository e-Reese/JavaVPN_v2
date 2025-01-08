package org.eugue.vpn.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VpnController{

    private static final Logger logger = LoggerFactory.getLogger(VpnController.class);
    
    @PostMapping("/vpn/start")
    public String StartVpn() {
        try {
            logger.info("Attempting to start vpn server...");
            
            // Execute the command
            Process process = new ProcessBuilder( "sudo", "openvpn", "--config", "/usr/local/etc/openvpn/server.conf").start();
            process.waitFor(); // Wait for the command to complete
            if (process.exitValue() != 0 ) {
                logger.info("Starting vpn exit value is NOT 0!");
            }
            logger.info("VPN server started");
            return "VPN server started";
        } catch (IOException | InterruptedException e) {
            return "Error starting VPN: " + e.getMessage(); 
        }
    }

    @PostMapping("/vpn/stop")
    public String StopVpn() {
        try {
            logger.info("Attempting to stop vpn server...");
            //stop vpn here
            String command = "sudo pkill openvpn";
            Process process = new ProcessBuilder(command.split(" ")).start();
            process.waitFor();
            logger.info("VPN Server Stopped");
            return "VPN Server Stopped";
        } catch (IOException | InterruptedException e) {
            logger.info("Error stopping vpn: " + e.getMessage());
            return "Error stopping vpn: " + e.getMessage();
        }
    }

    @GetMapping("/vpn/status")
    public String CheckVpnStatus() {
        try {
            logger.info("Checking vpn server status...");
            ProcessBuilder builder = new ProcessBuilder("pgrep", "openvpn");
            Process process = builder.start();
            process.waitFor();
            int exitValue = process.exitValue();
            logger.info(Integer.toString(exitValue));
            
            if (exitValue == 0) {
                logger.info("VPN is running");
                return "VPN is running.";
            }
            logger.info("VPN is not running");
            return "VPN is not running.";
        } catch (IOException | InterruptedException e) {
            return "Error checking VPN status: " + e.getMessage();
        }
    }

}
 
