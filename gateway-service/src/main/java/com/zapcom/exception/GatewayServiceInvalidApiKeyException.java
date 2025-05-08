
package com.zapcom.exception;

import org.springframework.http.HttpStatus;

public class GatewayServiceInvalidApiKeyException extends GatewayServiceException {
    
    public GatewayServiceInvalidApiKeyException(String message) {
        super(message, HttpStatus.UNAUTHORIZED.value());
    }
}
