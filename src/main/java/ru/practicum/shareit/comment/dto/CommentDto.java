package ru.practicum.shareit.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.Instant;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class CommentDto {
    private int id;
    @NotBlank
    private String text;
    @NotBlank
    private String authorName;
    @Past
    private Instant created;
}