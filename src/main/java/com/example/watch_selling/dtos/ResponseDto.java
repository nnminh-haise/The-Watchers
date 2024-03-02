package com.example.watch_selling.dtos;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> {
    private T data;

    private String message;

    private HttpStatus status;

    public ResponseDto<T> setData(T data) {
        this.data = data;
        return this;
    }

    public ResponseDto<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public ResponseDto<T> setStatus(HttpStatus status) {
        this.status = status;
        return this;
    }
}
