package ru.practicum.shareit.user.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserDto {
    private Long id;
    @NotBlank (message = "User name should be not blank or null")
    private String name;
    @NotBlank (message = "User email should be not blank or null")
    @Email (message = "Invalid email")
    private String email;

    public UserDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}