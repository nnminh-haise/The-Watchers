package com.example.watch_selling.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordDto {
    @NotEmpty
    private String currentPassword;

    @NotEmpty
    private String password;

    @NotEmpty
    private String confirmPassword;
}
