
package com.zapcom.model.request;

import java.util.Map;

public class GatewayServiceRequest {
    
    private String path;
    private String method;
    private Map<String, String> headers;
    private Object body;
    
    public GatewayServiceRequest() {
    }
    
    public GatewayServiceRequest(String path, String method, Map<String, String> headers, Object body) {
        this.path = path;
        this.method = method;
        this.headers = headers;
        this.body = body;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    
    public Map<String, String> getHeaders() {
        return headers;
    }
    
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
    
    public Object getBody() {
        return body;
    }
    
    public void setBody(Object body) {
        this.body = body;
    }
}
