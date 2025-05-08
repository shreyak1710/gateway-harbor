
package com.zapcom.exception;

import org.springframework.http.HttpStatus;

public class GatewayServiceMissingHeaderException extends GatewayServiceException {
    
    public GatewayServiceMissingHeaderException(String message) {
        super(message, HttpStatus.BAD_REQUEST.value());
    }
}
