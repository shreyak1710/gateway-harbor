
package com.zapcom.exception;

import org.springframework.http.HttpStatus;

public class GatewayServiceUnauthorizedException extends GatewayServiceException {
    
    public GatewayServiceUnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED.value());
    }
}
