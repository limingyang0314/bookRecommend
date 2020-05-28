package com.example.springwebserver.service;

import com.example.springwebserver.exception.BusinessException;

import java.util.HashMap;

public interface ReviewService {
    HashMap<String,String> addReview(Long bookId, String content, int star) throws BusinessException;
}
