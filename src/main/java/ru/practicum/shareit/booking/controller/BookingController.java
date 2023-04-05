package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String SHARER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestHeader(value = SHARER_ID) int userId,
                             @RequestBody @Valid BookingCreateDto bookingCreateDto) {
        return bookingService.create(userId, bookingCreateDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@PathVariable @Positive int bookingId,
                              @RequestParam boolean approved,
                              @RequestHeader(value = SHARER_ID) int userId) {
        return bookingService.approve(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@PathVariable @Positive int bookingId,
                              @RequestHeader(value = SHARER_ID) int userId) {
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllByBooker(@RequestParam(defaultValue = "ALL") String state,
                                           @RequestHeader(value = SHARER_ID) int userId) {
        return bookingService.getAllByBooker(state, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestParam(defaultValue = "ALL") String state,
                                          @RequestHeader(value = SHARER_ID) int userId) {
        return bookingService.getAllByOwner(state, userId);
    }
}
