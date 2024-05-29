package com.example.watch_selling.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountInformation {
    private UUID id;

    private String email;

    private String lastName;

    private String token;
}