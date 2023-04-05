package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto create(int userId, BookingCreateDto bookingCreateDto);
    BookingDto approve(int bookingId, boolean approved, int userId);
    BookingDto getById(int bookingId, int userId);
    List<BookingDto> getAllByBooker(String state, int userId);
    List<BookingDto> getAllByOwner(String state, int userId);
}
