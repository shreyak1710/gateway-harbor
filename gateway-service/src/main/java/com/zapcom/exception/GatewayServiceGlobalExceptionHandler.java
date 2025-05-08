
package com.zapcom.exception;

import com.zapcom.model.response.GatewayServiceErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GatewayServiceGlobalExceptionHandler {
    
    @ExceptionHandler(GatewayServiceException.class)
    public ResponseEntity<GatewayServiceErrorResponse> handleGatewayServiceException(GatewayServiceException ex) {
        GatewayServiceErrorResponse errorResponse = new GatewayServiceErrorResponse(
            ex.getStatusCode(),
            ex.getMessage(),
            System.currentTimeMillis()
        );
        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GatewayServiceErrorResponse> handleGenericException(Exception ex) {
        GatewayServiceErrorResponse errorResponse = new GatewayServiceErrorResponse(
            500,
            "An unexpected error occurred: " + ex.getMessage(),
            System.currentTimeMillis()
        );
        return ResponseEntity.status(500).body(errorResponse);
    }
}
