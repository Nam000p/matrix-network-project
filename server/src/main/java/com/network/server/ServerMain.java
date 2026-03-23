package com.network.server;

public class ServerMain {
    public static void main(String[] args) {
        System.out.println("=== KHOI DONG HE THONG SERVER SONG SONG ===");
        
        new Thread(() -> {
            try { 
            	ServerTCP.main(null); 
            } catch (Exception e) { 
            	System.err.println("Loi Server TCP: " + e.getMessage());
            }
        }).start();

        System.out.println("[+] UDP Server dang san sang...");
        ServerUDP.main(null);
    }
}