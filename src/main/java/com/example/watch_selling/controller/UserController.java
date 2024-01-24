package com.example.watch_selling.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/user")
    public String home() {
        return "user's APIs";
    }

    @PostMapping("/api/signup")
    public List<String> signup(@RequestBody Map<String, String> RequestBody) {
        String email = RequestBody.get("email");
        String password = RequestBody.get("password");

        return List.of(email, password);
    }
}