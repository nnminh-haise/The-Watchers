package com.example.watch_selling.dtos;

public class LoginAccountDto {
    private String email;

    private String password;

    public LoginAccountDto() {}

    public LoginAccountDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}
