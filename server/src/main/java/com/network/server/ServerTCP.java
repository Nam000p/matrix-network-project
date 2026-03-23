package com.network.server;

import com.google.gson.Gson;
import com.network.common.Matrix;
import com.network.common.Request;
import com.network.common.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP {
    private static final int PORT = 65432;

    public static void main(String[] args) {
        System.out.println("=== TCP SERVER DANG KHOI DONG ===");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[+] Server dang lang nghe tai cong " + PORT + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[+] Client moi ket noi: " + clientSocket.getInetAddress().getHostAddress());

                ClientHandler handler = new ClientHandler(clientSocket);
                new Thread(handler).start();
            }
        } catch (Exception e) {
            System.err.println("[-] Loi Server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private Gson gson;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.gson = new Gson();
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String jsonRequest;
            while ((jsonRequest = in.readLine()) != null) {
                System.out.println("\n[nhan] Request tu " + socket.getInetAddress() + ": " + jsonRequest);

                Response response = new Response();
                try {
                    Request req = gson.fromJson(jsonRequest, Request.class);
                    response = processRequest(req);
                } catch (Exception e) {
                    response.setStatus("ERROR");
                    response.setMessage("Loi xu ly: " + e.getMessage());
                }

                String jsonResponse = gson.toJson(response);
                System.out.println("[gui] Response ve " + socket.getInetAddress() + ": " + jsonResponse);
                out.println(jsonResponse);
            }
        } catch (Exception e) {
            System.err.println("[-] Loi ket noi voi Client " + socket.getInetAddress() + ": " + e.getMessage());
        } finally {
            try {
                socket.close();
                System.out.println("[-] Client " + socket.getInetAddress() + " da ngat ket noi.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Response processRequest(Request req) {
        Response res = new Response();
        res.setStatus("OK");
        res.setMessage("Thanh cong");

        String op = req.getOperation();
        Matrix m1 = null, m2 = null;

        if (req.getMat1() != null) m1 = new Matrix(req.getMat1());
        if (req.getMat2() != null) m2 = new Matrix(req.getMat2());

        try {
            switch (op) {
                case "CONG":
                    res.setResult(Matrix.cong(m1, m2).getData());
                    break;
                case "TRU":
                    res.setResult(Matrix.tru(m1, m2).getData());
                    break;
                case "NHAN":
                    res.setResult(Matrix.nhan(m1, m2).getData());
                    break;
                case "NHAN_SCALAR":
                    res.setResult(Matrix.nhanScalar(m1, req.getScalar()).getData());
                    break;
                case "CHUYEN_VI":
                    res.setResult(m1.chuyenVi().getData());
                    break;
                case "TRACE":
                    res.setTrace(m1.trace());
                    break;
                case "DOI_XUNG":
                    res.setIsSymmetric(m1.laDoiXung());
                    break;
                case "DINH_THUC":
                    res.setMessage("Dinh thuc: " + m1.dinhThuc());
                    break;
                case "NGHICH_DAO":
                    res.setResult(m1.nghichDao().getData());
                    break;
                case "HANG":
                    res.setMessage("Hang cua ma tran: " + m1.hang());
                    break;
                default:
                    res.setStatus("ERROR");
                    res.setMessage("Khong tim thay phep toan: " + op);
                    break;
            }
        } catch (IllegalArgumentException | ArithmeticException e) {
            res.setStatus("ERROR");
            res.setMessage(e.getMessage());
        }

        return res;
    }
}