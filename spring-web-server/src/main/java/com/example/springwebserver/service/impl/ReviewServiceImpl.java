package com.example.springwebserver.service.impl;

import com.example.springwebserver.dao.ReviewAgreeLogDOMapper;
import com.example.springwebserver.dao.ReviewDOMapper;
import com.example.springwebserver.dataObject.ReviewAgreeLogDOKey;
import com.example.springwebserver.dataObject.ReviewDO;
import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.service.BookService;
import com.example.springwebserver.service.ReviewService;
import com.example.springwebserver.service.UserService;
import com.example.springwebserver.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("ReviewService")
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewDOMapper reviewDOMapper;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewAgreeLogDOMapper reviewAgreeLogDOMapper;

    public HashMap<String,String> addReview(Long bookId, String content, int star) throws BusinessException {
        UserModel user = userService.getUserByToken();
        ReviewDO review = new ReviewDO();
        review.setBookId(bookId);
        review.setContent(content);
        review.setStar(star);
        review.setUserId(user.getUserId());
        reviewDOMapper.insertSelective(review);
        HashMap<String,String> ret = new HashMap<String,String>();
        ret.put("message","Add review success.");
        return ret;
    }

//    public HashMap<String,String> agreeReview(Long review_id) throws BusinessException {
//        UserModel user = userService.getUserByToken();
//        ReviewAgreeLogDOKey key = new ReviewAgreeLogDOKey();
//        key.setReviewId(review_id);
//        key.setUserId(user.getUserId());
//        if(reviewAgreeLogDOMapper.)
//
//
//    }
}
