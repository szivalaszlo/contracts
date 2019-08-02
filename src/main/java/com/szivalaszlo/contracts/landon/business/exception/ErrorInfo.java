package com.szivalaszlo.contracts.landon.business.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.List;

public class ErrorInfo {
    private Timestamp timestamp;
    private int status;
    private String error;
    private List<String> message;
    private String path;
    private HttpStatus httpStatus;
    private HttpHeaders httpHeaders;

    public ErrorInfo(HttpStatus httpStatus, String path, List<String> message){
        this.path = path;
        this.message = message;
        timestamp = new Timestamp(System.currentTimeMillis());
        this.httpStatus = httpStatus;
        error = httpStatus.getReasonPhrase();
        status = httpStatus.value();
    }

    public ErrorInfo(HttpStatus httpStatus, String path, List<String> message, HttpHeaders httpHeaders){
        this.path = path;
        this.message = message;
        timestamp = new Timestamp(System.currentTimeMillis());
        this.httpStatus = httpStatus;
        error = httpStatus.getReasonPhrase();
        status = httpStatus.value();
        this.httpHeaders = httpHeaders;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ErrorInfo{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", error='" + error + '\'' +
                ", message=" + message +
                ", path='" + path + '\'' +
                ", httpStatus=" + httpStatus +
                '}';
    }
}
