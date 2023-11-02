package com.example.repo;

import com.example.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArtistRepo extends JpaRepository<Artist, Long> {

    @Query("SELECT a FROM Artist a WHERE a.name LIKE %:name%")
    List<Artist> findByName(@Param("name") String name);

}
