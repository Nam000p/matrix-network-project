package com.network.server;

import com.google.gson.Gson;
import com.network.common.Matrix;
import com.network.common.Request;
import com.network.common.Response;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerUDP {
	private static final int PORT = 65433;
	private static final int BUFFER_SIZE = 65535;
	private static final Gson gson = new Gson();

	public static void main(String[] args) {
		System.out.println("=== UDP SERVER DANG KHOI DONG ===");

		try (DatagramSocket socket = new DatagramSocket(PORT)) {
			System.out.println("[+] Server UDP dang lang nghe tai cong " + PORT);
			byte[] receiveBuffer = new byte[BUFFER_SIZE];

			while (true) {
				DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
				socket.receive(receivePacket);

				String jsonRequest = new String(receivePacket.getData(), 0, receivePacket.getLength());
				InetAddress clientAddress = receivePacket.getAddress();
				int clientPort = receivePacket.getPort();

				System.out.println("[!] Nhan yeu cau UDP tu: " + clientAddress + ":" + clientPort);

				Response response = process(jsonRequest);

				byte[] sendBuffer = gson.toJson(response).getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress,
						clientPort);
				socket.send(sendPacket);
			}
		} catch (Exception e) {
			System.err.println("[-] Loi Server UDP: " + e.getMessage());
		}
	}

	private static Response process(String jsonRequest) {
		Response res = new Response();
		try {
			Request req = gson.fromJson(jsonRequest, Request.class);
			Matrix a = new Matrix(req.getMat1());
			res.setStatus("OK");

			switch (req.getOperation()) {
			case "CONG" -> res.setResult(Matrix.cong(a, new Matrix(req.getMat2())).getData());
			case "TRU" -> res.setResult(Matrix.tru(a, new Matrix(req.getMat2())).getData());
			case "NHAN" -> res.setResult(Matrix.nhan(a, new Matrix(req.getMat2())).getData());
			case "NHAN_SCALAR" -> res.setResult(Matrix.nhanScalar(a, req.getScalar()).getData());
			case "CHUYEN_VI" -> res.setResult(a.chuyenVi().getData());
			case "TRACE" -> res.setTrace(a.trace());
			case "DOI_XUNG" -> res.setIsSymmetric(a.laDoiXung());
			case "DINH_THUC" -> res.setTrace(a.dinhThuc());
			case "HANG" -> res.setTrace((double) a.hang());
			default -> {
				res.setStatus("ERROR");
				res.setMessage("Phep toan khong hop le");
			}
			}
		} catch (Exception e) {
			res.setStatus("ERROR");
			res.setMessage("Loi: " + e.getMessage());
		}
		return res;
	}
}