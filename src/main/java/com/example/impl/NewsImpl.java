package com.example.impl;

import com.example.dao.NewsDao;
import com.example.model.News;
import com.example.repo.NewsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


public class NewsImpl implements NewsDao {

    private NewsRepo newsRepo;

    @Override
    public List<News> findAllNews() {
        return this.newsRepo.findAll();
    }
}
