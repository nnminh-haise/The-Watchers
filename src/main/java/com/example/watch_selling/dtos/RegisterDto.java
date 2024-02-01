package com.example.watch_selling.dtos;

public class RegisterDto {
    private String email;

    private String password;

    private String confirmPassword;

    public RegisterDto() {}

    public RegisterDto(String email, String password, String confirmPassword) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public String getConfirmPassword() { return confirmPassword; }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }

    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}
