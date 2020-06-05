package com.xw.poseidon.entity;

public enum ResultEnum {
	SUCCESS("200","ok"),
	FAILURE("400","failure");
	
    private String code;
	private String message;
	private ResultEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
