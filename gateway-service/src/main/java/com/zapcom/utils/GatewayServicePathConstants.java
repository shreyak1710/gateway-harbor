
package com.zapcom.utils;

public class GatewayServicePathConstants {
    
    // Paths that don't require API key authentication
    public static final String[] OPEN_API_ENDPOINTS = {
            "/auth/api/auth/register",
            "/auth/api/auth/verify-email",
            "/auth/api/auth/login",
            "/auth/api/auth/generate-api-key",
            "/fallback"
    };
    
    // Private constructor to prevent instantiation
    private GatewayServicePathConstants() {
    }
}
