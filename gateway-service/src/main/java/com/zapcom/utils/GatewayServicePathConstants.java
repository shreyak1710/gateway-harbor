
package com.zapcom.utils;

public class GatewayServicePathConstants {
    
    // API Path Constants
    public static final String AUTH_PATH = "/auth";
    public static final String CUSTOMER_PATH = "/api/customers";
    public static final String FALLBACK_PATH = "/fallback";
    
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
