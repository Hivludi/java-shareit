package ru.practicum.shareit.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class CommentCreateDto {
    @NotBlank
    private String text;
}