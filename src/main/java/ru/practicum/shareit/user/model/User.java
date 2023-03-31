package ru.practicum.shareit.user.model;

import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class User {
    private int id;
    private final String name;
    private final String email;
}