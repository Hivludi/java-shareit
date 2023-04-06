package ru.practicum.shareit.booking.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
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
    @Getter
    @Setter
    public static class Item {
        private int id;
        private String name;
    }
}
