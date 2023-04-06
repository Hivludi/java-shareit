package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDto {
    private int id;
    private String name;
    @NotNull(message = "У пользователя должна быть электронная почта")
    @Email(message = "Электронная почта невалидна")
    private String email;
}