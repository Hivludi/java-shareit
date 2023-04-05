package ru.practicum.shareit.comment.dto;

import javax.validation.constraints.NotBlank;

public class CommentCreateDto {
    @NotBlank
    private String text;

    public CommentCreateDto() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}