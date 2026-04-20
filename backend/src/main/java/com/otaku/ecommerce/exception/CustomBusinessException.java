package com.otaku.ecommerce.exception;

public class CustomBusinessException extends RuntimeException {
    
    private final String internalCode;
    private final int statusCode;

    public CustomBusinessException(String internalCode, String message, int statusCode) {
        super(message);
        this.internalCode = internalCode;
        this.statusCode = statusCode;
    }

    public String getInternalCode() {
        return internalCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
