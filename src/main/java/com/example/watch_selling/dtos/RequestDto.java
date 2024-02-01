package com.example.watch_selling.dtos;

public class RequestDto<T> {
    private T data;

    public RequestDto() {}

    public RequestDto(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
