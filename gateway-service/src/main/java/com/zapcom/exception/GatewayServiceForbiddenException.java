
package com.zapcom.exception;

import org.springframework.http.HttpStatus;

public class GatewayServiceForbiddenException extends GatewayServiceException {
    
    public GatewayServiceForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN.value());
    }
}
