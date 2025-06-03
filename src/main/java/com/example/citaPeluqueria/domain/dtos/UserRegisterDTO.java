package com.example.citaPeluqueria.domain.dtos;

import com.example.citaPeluqueria.util.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {
    private String username;
    @Email
    @NotBlank(message = Constants.THE_EMAIL_IS_REQUIRED)
    private String email;
    private String phone;
    @NotBlank(message = Constants.PASSWORD_IS_REQUIRED)
    private String password;

    private String confirmPassword;
}
