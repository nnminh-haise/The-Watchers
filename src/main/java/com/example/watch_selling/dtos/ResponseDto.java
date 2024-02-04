package com.example.watch_selling.dtos;

public class ResponseDto<T> {
    private T data;

    private String message;

    private String description;

    private Integer status;

    public ResponseDto() {}

    public ResponseDto(T data) {
        this.data = data;
    }

    public ResponseDto(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public ResponseDto(T data, String message, Integer status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }

    public ResponseDto(T data, String message, String description, Integer status) {
        this.data = data;
        this.message = message;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
