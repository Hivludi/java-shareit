package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingInfoDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank (message = "Item name should be not blank")
    private String name;
    @NotBlank (message = "Item description should be not blank or null")
    @Size(max = 200, message = "Item description should be not longer than 200 letters")
    private String description;
    @NotNull (message = "Item available should be not null")
    private Boolean available;
    private Long owner;
    private Long requestId;
    private BookingInfoDto lastBooking;
    private BookingInfoDto nextBooking;
    private List<CommentDto> comments;

    public ItemDto() {
    }

    public ItemDto(Long id, String name, String description, Boolean available, Long userId, Long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = userId;
        this.requestId = requestId;
    }
}