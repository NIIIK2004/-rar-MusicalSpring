package com.example.dao;

import com.example.model.News;

import java.util.List;

public interface NewsDao {
    public List<News> findAllNews();
}

