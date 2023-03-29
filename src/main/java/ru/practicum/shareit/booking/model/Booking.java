package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class Booking {
    private int id;
    private final LocalDate start;
    private final LocalDate end;
    private final Item item;
    private final User booker;
    private final BookingStatus status;
}