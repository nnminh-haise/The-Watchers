package com.example.watch_selling.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileDto {
    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private String gender;

    private String address;

    private String phoneNumber;

    private String photo;

    private String citizenId;
}
