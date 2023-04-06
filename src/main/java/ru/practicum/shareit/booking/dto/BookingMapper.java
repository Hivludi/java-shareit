package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BookingMapper {

    public static Booking toBooking(BookingCreateDto bookingCreateDto, Item item, User user) {
        return Booking.builder()
                .start(bookingCreateDto.getStart())
                .end(bookingCreateDto.getEnd())
                .item(item)
                .booker(user)
                .build();
    }

    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemMapper.toItemDto(booking.getItem()))
                .booker(UserMapper.toUserDto(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

    public static List<BookingDto> toListOfBookingDto(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    public static BookingForItemDto toBookingDtoForItem(Booking booking) {
        return BookingForItemDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(new BookingForItemDto.Item(booking.getItem().getId(), booking.getItem().getName()))
                .bookerId(booking.getBooker().getId())
                .build();
    }
}