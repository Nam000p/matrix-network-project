package com.network.common;

import com.google.gson.annotations.SerializedName;

public class Response {
	@SerializedName("status")
	private String status;
	@SerializedName("result")
	private double[][] result;
	@SerializedName("trace")
	private Double trace;
	@SerializedName("isSymmetric")
	private Boolean isSymmetric;
	@SerializedName("message")
	private String message;

	public Response() {
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double[][] getResult() {
		return result;
	}

	public void setResult(double[][] result) {
		this.result = result;
	}

	public Double getTrace() {
		return trace;
	}

	public void setTrace(Double trace) {
		this.trace = trace;
	}

	public Boolean getIsSymmetric() {
		return isSymmetric;
	}

	public void setIsSymmetric(Boolean isSymmetric) {
		this.isSymmetric = isSymmetric;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}