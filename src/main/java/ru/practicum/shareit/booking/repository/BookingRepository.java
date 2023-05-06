package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findFirstByItemIdIsAndEndIsBeforeOrderByEndDesc(Long id, LocalDateTime now);

    Optional<Booking> findFirstByItemIdIsAndStartIsAfterOrderByStartAsc(Long id, LocalDateTime now);

    List<Booking> findAllByBookerIdOrderByIdDesc(Long id, Pageable pageable);

    List<Booking> findAllByItemIdIn(List<Long> ids);

    @Query(" SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.status = 'REJECTED' " +
            "ORDER BY b.id DESC")
    List<Booking> findAllRejected(Long id);

    @Query(" SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.status = 'WAITING' " +
            "ORDER BY b.id DESC")
    List<Booking> findAllWaiting(Long id);

    @Query(" SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.start > ?2 " +
            "ORDER BY b.id DESC")
    List<Booking> findAllFutureBookings(Long id, LocalDateTime now);

    @Query(" SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.end < ?2 " +
            "ORDER BY b.id DESC")
    List<Booking> findAllPastBookings(Long id, LocalDateTime now);

    @Query(" SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.start < ?2 " +
            "AND b.end > ?2 " +
            "ORDER BY b.id DESC")
    List<Booking> findAllCurrentBookings(Long id, LocalDateTime now);

    @Query(" SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "ORDER BY b.id DESC")
    List<Booking> findAllOwnersBookings(Long id, Pageable pageable);

    @Query(" SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.end < ?2 " +
            "ORDER BY b.id DESC")
    List<Booking> findAllOwnersPastBookings(Long id, LocalDateTime now);

    @Query(" SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.start > ?2 " +
            "ORDER BY b.id DESC")
    List<Booking> findAllOwnersFutureBookings(Long id, LocalDateTime now);

    @Query(" SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.start < ?2 " +
            "AND b.end > ?2 " +
            "ORDER BY b.id DESC")
    List<Booking> findAllOwnersCurrentBookings(Long id, LocalDateTime now);

    @Query(" SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.status = 'REJECTED' " +
            "ORDER BY b.id DESC")
    List<Booking> findAllOwnersRejected(Long id);

    @Query(" SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.status = 'APPROVED' " +
            "ORDER BY b.id DESC")
    List<Booking> findFirstOwnersApproved(Long id);

    @Query(" SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.status = 'WAITING' " +
            "ORDER BY b.id DESC")
    List<Booking> findAllOwnersWaiting(Long id);
}