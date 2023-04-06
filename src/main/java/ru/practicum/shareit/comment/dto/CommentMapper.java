package ru.practicum.shareit.comment.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.comment.model.Comment;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentMapper {

    public static Comment toComment(CommentCreateDto commentCreateDto) {
        return Comment.builder()
                .text(commentCreateDto.getText())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }
}
