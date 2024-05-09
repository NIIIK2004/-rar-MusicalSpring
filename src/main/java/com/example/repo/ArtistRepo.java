package com.example.repo;

import com.example.model.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface ArtistRepo extends JpaRepository<Artist, Long> {

    @Query("SELECT a FROM Artist a WHERE a.name LIKE %:name%")
    List<Artist> findByName(@Param("name") String name);

    boolean existsByName(String name);
    List<Artist> findByGenreAndIdNotOrderByListenersDesc(String genre, Long id);

    long countByCreatedDateBetween(LocalDateTime start, LocalDateTime end);


}
