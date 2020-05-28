package com.example.springwebserver.service;

import com.example.springwebserver.controller.viewObject.ReviewVO;
import com.example.springwebserver.dataObject.ReviewDO;
import com.example.springwebserver.exception.BusinessException;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

public interface ReviewService {
    HashMap<String,String> addReview(Long bookId, String content, int star) throws BusinessException;

    List<ReviewVO> getReviewByBookIdDescByTime(Long bookId, int page, int size) throws BusinessException;

    List<ReviewVO> getReviewByBookIdDescByAgreeNum(Long bookId, int page, int size) throws BusinessException;

    HashMap<String,String> delReview(Long reviewId) throws BusinessException;

    HashMap<String,String> agreeReview(Long reviewId) throws BusinessException;

//    List<ReviewDO>
}
