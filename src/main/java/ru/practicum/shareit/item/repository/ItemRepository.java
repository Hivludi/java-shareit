package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findByOwner(int ownerId);

    @Query("SELECT i FROM Item i" +
            " WHERE LOWER(i.name) LIKE (CONCAT('%', ?1, '%'))" +
            " OR LOWER(i.description) LIKE (CONCAT('%', ?1, '%'))" +
            " AND i.available = TRUE")
    List<Item> findAvailableItemsByNameOrDescription(String text);
}