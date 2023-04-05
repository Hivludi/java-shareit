package ru.practicum.shareit.item.dto;

import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.comment.dto.CommentDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ItemDtoWithBookingsAndComments {
    private int id;
    @NotBlank(message = "name field cannot be empty")
    private String name;
    @NotBlank(message = "description field cannot be empty")
    private String description;
    @BooleanFlag
    @NotNull(message = "available field cannot be empty")
    private Boolean available;
    private BookingForItemDto lastBooking;
    private BookingForItemDto nextBooking;
    private List<CommentDto> comments;
}