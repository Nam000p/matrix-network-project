package com.network.common;

import com.google.gson.annotations.SerializedName;

public class Request {
	@SerializedName("op")
	private String operation;
	@SerializedName("mat1")
	private double[][] mat1;
	@SerializedName("mat2")
	private double[][] mat2;
	@SerializedName("scalar")
	private double scalar;

	public Request() {
	}

	public Request(String op, double[][] m1, double[][] m2, double s) {
		this.operation = op;
		this.mat1 = m1;
		this.mat2 = m2;
		this.scalar = s;
	}

	public String getOperation() {
		return operation;
	}

	public double[][] getMat1() {
		return mat1;
	}

	public double[][] getMat2() {
		return mat2;
	}

	public double getScalar() {
		return scalar;
	}
}