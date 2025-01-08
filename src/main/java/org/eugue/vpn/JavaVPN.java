/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package org.eugue.vpn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author ethanreese
 */

@SpringBootApplication
public class JavaVPN {

    public static void main(String[] args) {
        System.out.println("Loading VPN Application...");
        SpringApplication.run(JavaVPN.class, args);

        System.out.println("VPN Application is running!");
    }
}
