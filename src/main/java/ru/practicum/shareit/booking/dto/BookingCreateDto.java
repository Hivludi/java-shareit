package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.validation.DateValidator;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@DateValidator
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class BookingCreateDto {
    @Positive
    @NotNull
    private int itemId;
    @FutureOrPresent
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
}