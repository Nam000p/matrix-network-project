package com.network.client;

import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n========== MATRIX SYSTEM ==========");
            System.out.println("1. Su dung giao thuc TCP (On dinh)");
            System.out.println("2. Su dung giao thuc UDP (Nhanh)");
            System.out.println("0. Thoat chuong trinh");
            System.out.print(">>> Chon giao thuc: ");
            
            String choice = sc.nextLine().trim();
            if (choice.isEmpty()) continue;

            switch (choice) {
                case "1" -> ClientTCP.main(null);
                case "2" -> ClientUDP.main(null);
                case "0" -> {
                    System.out.println("Tam biet!");
                    return;
                }
                default -> System.out.println("[-] Lua chon khong hop le!");
            }
        }
    }
}