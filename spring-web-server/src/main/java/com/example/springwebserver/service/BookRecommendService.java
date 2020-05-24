package com.example.springwebserver.service;

import com.example.springwebserver.service.model.BookModel;
import com.example.springwebserver.service.model.BookRecommendModel;

import java.util.List;

public interface BookRecommendService {
    List<BookModel> listBookRecommendByBookID(Long bookID, int page, int size);
}
