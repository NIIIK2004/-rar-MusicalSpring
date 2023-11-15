package com.example.repo;

import com.example.model.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepo extends JpaRepository<News, Long> {

    void deleteById(Long id);
}
