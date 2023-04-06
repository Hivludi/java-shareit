package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findAllByBookerId(int userId, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 " +
            " AND CURRENT_TIMESTAMP BETWEEN b.start AND b.end")
    List<Booking> findAllByBookerIdAndCurrentState(int userId, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 " +
            " AND b.end < CURRENT_TIMESTAMP")
    List<Booking> findAllByBookerIdAndPastState(int userId, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 " +
            " AND b.start > CURRENT_TIMESTAMP")
    List<Booking> findAllByBookerIdAndFutureState(int userId, Sort sort);

    List<Booking> findAllByBookerIdAndStatus(int userId, Status status, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.item.owner = ?1")
    List<Booking> findAllByOwnerId(int userId, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.item.owner = ?1 " +
            " AND CURRENT_TIMESTAMP BETWEEN b.start AND b.end")
    List<Booking> findAllByOwnerIdAndCurrentState(int userId, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.item.owner = ?1 " +
            " AND b.end < CURRENT_TIMESTAMP")
    List<Booking> findAllByOwnerIdAndPastState(int userId, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.item.owner = ?1 " +
            " AND b.start > CURRENT_TIMESTAMP")
    List<Booking> findAllByOwnerIdAndFutureState(int userId, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.item.owner = ?1 AND b.status = ?2")
    List<Booking> findAllByOwnerIdAndWaitingOrRejectedState(int userId, Status status, Sort sort);

    List<Booking> findAllByItemIdIn(List<Integer> itemsId);
}
