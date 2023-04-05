package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Builder
@Getter @Setter
@AllArgsConstructor
public class BookingForItemDto {
    @Positive
    private int id;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    @NotNull
    private Item item;
    @Positive
    private int bookerId;

    @AllArgsConstructor
    @Getter @Setter
    public static class Item {
        private int id;
        private String name;
    }
}
