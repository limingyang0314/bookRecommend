package com.example.springwebserver.service;

import com.example.springwebserver.service.model.BookModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SearchService {
    List<BookModel> searchByKey(String key, int page, int size);
}
