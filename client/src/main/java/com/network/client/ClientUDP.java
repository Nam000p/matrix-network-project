package com.network.client;

import com.google.gson.Gson;
import com.network.common.Request;
import com.network.common.Response;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class ClientUDP {
    private static final String HOST = System.getenv("SERVER_HOST") != null ? System.getenv("SERVER_HOST") : "localhost";
    private static final int PORT = 65433;
    private static final int BUFFER_SIZE = 65535;
    private static final Scanner sc = new Scanner(System.in);
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        System.out.println("=== CLIENT UDP KET NOI TOI: " + HOST + " ===");

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName(HOST);
            socket.setSoTimeout(5000);

            while (true) {
                System.out.print("\n>>> Chon phep toan UDP (1-10, 0 de thoat): ");
                String input = sc.nextLine().trim();
                if (input.isEmpty()) {
                	continue;
                }
                int choice = Integer.parseInt(input);
                if (choice == 0) {
                	break;
                }
                Request request = createRequest(choice);
                if (request == null) {
                	continue;
                }
                byte[] sendBuffer = gson.toJson(request).getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, PORT);
                socket.send(sendPacket);

                byte[] receiveBuffer = new byte[BUFFER_SIZE];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);

                String jsonResponse = new String(receivePacket.getData(), 0, receivePacket.getLength());
                Response response = gson.fromJson(jsonResponse, Response.class);
                handleResponse(response);
            }
        } catch (SocketTimeoutException e) {
            System.err.println("[-] Loi: Server UDP khong phan hoi (Timeout)!");
        } catch (Exception e) {
            System.err.println("[-] Loi Client UDP: " + e.getMessage());
        }
    }

    private static Request createRequest(int choice) {
        String op = switch(choice) {
            case 1 -> "CONG";
            case 2 -> "TRU";
            case 3 -> "NHAN";
            case 4 -> "NHAN_SCALAR";
            case 5 -> "CHUYEN_VI";
            case 6 -> "TRACE";
            case 7 -> "DOI_XUNG";
            case 8 -> "DINH_THUC";
            case 9 -> "NGHICH_DAO";
            case 10 -> "HANG";
            default -> "";
        };

        if (op.isEmpty()) return null;

        try {
            System.out.print("Nhap so hang: ");
            int r = Integer.parseInt(sc.nextLine());
            System.out.print("Nhap so cot: ");
            int c = Integer.parseInt(sc.nextLine());

            double[][] m1 = inputMatrix(r, c, "A");
            double[][] m2 = (choice <= 3) ? inputMatrix(r, c, "B") : null;

            double s = 0;
            if (choice == 4) {
                System.out.print("Nhap gia tri vo huong: ");
                s = Double.parseDouble(sc.nextLine());
            }

            return new Request(op, m1, m2, s);
        } catch (Exception e) {
            System.out.println("[-] Loi nhap lieu!");
            return null;
        }
    }

    private static double[][] inputMatrix(int r, int c, String name) {
        double[][] m = new double[r][c];
        for(int i=0; i<r; i++) {
        	for(int j=0; j<c; j++) {
        		System.out.print(name+"["+i+"]["+j+"]: "); m[i][j] = Double.parseDouble(sc.nextLine());
        	}
        }
        return m;
    }

    private static void handleResponse(Response res) {
        if(!"OK".equals(res.getStatus())) {
            System.out.println("[-] Loi Server: " + res.getMessage());
            return;
        }

        System.out.println("[+] Ket qua UDP:");

        if(res.getMessage() != null && !res.getMessage().equals("Thanh cong")) {
            System.out.println("> Thong bao: " + res.getMessage());
        }

        if(res.getIsSymmetric() != null) {
            System.out.println("> Doi xung: " + (res.getIsSymmetric() ? "Co" : "Khong"));
        }

        if(res.getTrace() != null) {
            System.out.println("> Gia tri so: " + res.getTrace());
        }

        if(res.getResult() != null) {
            for(double[] row : res.getResult()) {
                for(double v : row) {
                    System.out.printf("%8.2f ", v);
                }
                System.out.println();
            }
        }
    }
}