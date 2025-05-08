
package com.zapcom.controller;

import com.zapcom.model.response.GatewayServiceErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/auth")
    public ResponseEntity<GatewayServiceErrorResponse> authServiceFallback() {
        GatewayServiceErrorResponse response = new GatewayServiceErrorResponse(
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            "Authentication Service is currently unavailable. Please try again later.",
            System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
    
    @GetMapping("/customers")
    public ResponseEntity<GatewayServiceErrorResponse> customerServiceFallback() {
        GatewayServiceErrorResponse response = new GatewayServiceErrorResponse(
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            "Customer Service is currently unavailable. Please try again later.",
            System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}
