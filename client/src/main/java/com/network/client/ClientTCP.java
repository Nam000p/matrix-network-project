package com.network.client;

import com.google.gson.Gson;
import com.network.common.Request;
import com.network.common.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientTCP {
	private static final String HOST = System.getenv("SERVER_HOST") != null ? System.getenv("SERVER_HOST")
			: "localhost";;
	private static final int PORT = 65432;
	private static final Scanner sc = new Scanner(System.in);
	private static final Gson gson = new Gson();

	public static void main(String[] args) {
		System.out.println("=== DANG KET NOI TOI SERVER TCP... ===");

		try (Socket socket = new Socket(HOST, PORT);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

			System.out.println("[+] Ket noi thanh cong!");

			while (true) {
				printMenu();
				System.out.print(">>> Chon phep toan (0 de thoat): ");
				String input = sc.nextLine().trim();
				if (input.isEmpty()) {
					continue;
				}
				try {
					int choice = Integer.parseInt(input);
					if (choice == 0) {
						break;
					}
					Request request = createRequest(choice);
					if (request == null) {
						continue;
					}
					String jsonRequest = gson.toJson(request);
					out.println(jsonRequest);
					String jsonResponse = in.readLine();
					Response response = gson.fromJson(jsonResponse, Response.class);
					handleResponse(response);
				} catch (NumberFormatException e) {
					System.out.println("[-] Vui long nhap mot con so hop le!");
				}

			}
		} catch (Exception e) {
			System.err.println("[-] Loi Client: " + e.getMessage());
		}
	}

	private static void printMenu() {
		System.out.println("\n--- MENU PHEP TOAN MA TRAN ---");
		System.out.println("1. CONG 2 ma tran");
		System.out.println("2. TRU 2 ma tran");
		System.out.println("3. NHAN 2 ma tran");
		System.out.println("4. NHAN voi vo huong (Scalar)");
		System.out.println("5. CHUYEN VI ma tran");
		System.out.println("6. TINH TRACE");
		System.out.println("7. KIEM TRA DOI XUNG");
		System.out.println("8. TINH DINH THUC");
		System.out.println("9. TIM MA TRAN NGHICH DAO");
		System.out.println("10. TINH HANG (RANK)");
		System.out.println("0. Thoat");
	}

	private static Request createRequest(int choice) {
		String op = switch (choice) {
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
		default -> null;
		};
		if (op == null) {
			return null;
		}

		System.out.print("Nhap so hang: ");
		int r = Integer.parseInt(sc.nextLine());
		System.out.print("Nhap so cot: ");
		int c = Integer.parseInt(sc.nextLine());
		double[][] m1 = inputMatrix(r, c, "A");
		double[][] m2 = null;
		double s = 0;

		if (choice >= 1 && choice <= 3) {
			System.out.println("Nhap ma tran thu 2:");
			m2 = inputMatrix(r, c, "B");
		} else if (choice == 4) {
			System.out.print("Nhap gia tri vo huong (scalar): ");
			s = Double.parseDouble(sc.nextLine());
		}
		return new Request(op, m1, m2, s);
	}

	private static double[][] inputMatrix(int r, int c, String name) {
		double[][] data = new double[r][c];
		System.out.println("Nhap ma tran " + name + ":");
		for (int i = 0; i < r; i++) {
			for (int j = 0; j < c; j++) {
				System.out.print(name + "[" + i + "][" + j + "] = ");
				data[i][j] = Double.parseDouble(sc.nextLine());
			}
		}
		return data;
	}

	private static void handleResponse(Response res) {
		if (!"OK".equals(res.getStatus())) {
			System.err.println("[-] LOI TU SERVER: " + res.getMessage());
			return;
		}

		System.out.println("[+] Ket qua tu Server:");
		if (res.getMessage() != null) {
			System.out.println("Thong bao: " + res.getMessage());
		}
		if (res.getTrace() != null) {
			System.out.println("Trace = " + res.getTrace());
		}
		if (res.getIsSymmetric() != null) {
			System.out.println("Doi xung: " + res.getIsSymmetric());
		}
		if (res.getResult() != null) {
			double[][] data = res.getResult();
			for (double[] row : data) {
				for (double val : row)
					System.out.printf("%8.2f ", val);
				System.out.println();
			}
		}
	}
}