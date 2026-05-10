package com.eoms.api.dto;

public class ApiMessageResponse {
    public boolean success;
    public String message;

    public ApiMessageResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}