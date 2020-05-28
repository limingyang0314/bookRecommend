package com.example.springwebserver.service;

import com.example.springwebserver.dataObject.ReviewDO;
import com.example.springwebserver.exception.BusinessException;

import java.util.HashMap;
import java.util.List;

public interface ReviewService {
    HashMap<String,String> addReview(Long bookId, String content, int star) throws BusinessException;

//    List<ReviewDO>
}
