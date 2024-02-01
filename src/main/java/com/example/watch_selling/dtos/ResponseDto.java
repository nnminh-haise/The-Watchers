package com.example.watch_selling.dtos;

public class ResponseDto<T> {
    private T data;

    private String message;

    private String description;

    private String error;

    private Integer statusCode;

    public ResponseDto() {}

    public ResponseDto(T data) {
        this.data = data;
    }

    public ResponseDto(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public ResponseDto(T data, String message, String error, Integer statusCode) {
        this.data = data;
        this.message = message;
        this.error = error;
        this.statusCode = statusCode;
    }

    public ResponseDto(T data, String message, String description, String error, Integer statusCode) {
        this.data = data;
        this.message = message;
        this.description = description;
        this.error = error;
        this.statusCode = statusCode;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
