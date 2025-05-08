
package com.zapcom.model.response;

public class GatewayServiceResponse<T> {
    
    private int status;
    private T data;
    private String message;
    private long timestamp;
    
    public GatewayServiceResponse() {
    }
    
    public GatewayServiceResponse(int status, T data, String message, long timestamp) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.timestamp = timestamp;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
