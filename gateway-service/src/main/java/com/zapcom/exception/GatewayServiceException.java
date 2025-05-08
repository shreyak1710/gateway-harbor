
package com.zapcom.exception;

public class GatewayServiceException extends RuntimeException {
    
    private int statusCode;
    
    public GatewayServiceException(String message) {
        super(message);
        this.statusCode = 500;
    }
    
    public GatewayServiceException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
}
