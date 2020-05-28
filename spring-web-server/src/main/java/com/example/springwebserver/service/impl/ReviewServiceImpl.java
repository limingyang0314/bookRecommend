package com.example.springwebserver.service.impl;

import com.example.springwebserver.controller.viewObject.ReviewVO;
import com.example.springwebserver.dao.ReviewAgreeLogDOMapper;
import com.example.springwebserver.dao.ReviewDOMapper;
import com.example.springwebserver.dataObject.ReviewAgreeLogDO;
import com.example.springwebserver.dataObject.ReviewAgreeLogDOKey;
import com.example.springwebserver.dataObject.ReviewDO;
import com.example.springwebserver.enums.EmBusinessError;
import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.response.CommonReturnType;
import com.example.springwebserver.service.BookService;
import com.example.springwebserver.service.ReviewService;
import com.example.springwebserver.service.UserService;
import com.example.springwebserver.service.model.UserModel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @Override
    public List<ReviewVO> getReviewByBookIdDescByTime(Long bookId, int page, int size) throws BusinessException{
        PageHelper.startPage(page, size);
        Page<ReviewDO> reviewPage = reviewDOMapper.listReviewByBookIDDescByReviewTime(bookId);
        List<ReviewDO> reviewList = reviewPage.getResult();

        List<ReviewVO> ret = new ArrayList<ReviewVO>();

        boolean login = userService.isLoginUser();
        UserModel user = new UserModel();
        if(login){
            user = userService.getUserByToken();
        }
        ReviewAgreeLogDOKey key = new ReviewAgreeLogDOKey();
        for(ReviewDO one: reviewList){
            ReviewVO temp = new ReviewVO();
            BeanUtils.copyProperties(one,temp);
            if(login){
                key.setReviewId(one.getReviewId());
                key.setUserId(user.getUserId());
                if(reviewAgreeLogDOMapper.selectByPrimaryKey(key) != null)
                    temp.setHasAgree(true);
                else
                    temp.setHasAgree(false);
            }else{
                temp.setHasAgree(false);
            }
            ret.add(temp);
        }
        return ret;
    }

    @Override
    public List<ReviewVO> getReviewByBookIdDescByAgreeNum(Long bookId, int page, int size) throws BusinessException{
        PageHelper.startPage(page, size);
        Page<ReviewDO> reviewPage = reviewDOMapper.listReviewByBookIDDescByAgreeNum(bookId);
        List<ReviewDO> reviewList = reviewPage.getResult();

        List<ReviewVO> ret = new ArrayList<ReviewVO>();

        boolean login = userService.isLoginUser();
        UserModel user = new UserModel();
        if(login){
            user = userService.getUserByToken();
        }
        ReviewAgreeLogDOKey key = new ReviewAgreeLogDOKey();
        for(ReviewDO one: reviewList){
            ReviewVO temp = new ReviewVO();
            BeanUtils.copyProperties(one,temp);
            if(login){
                key.setReviewId(one.getReviewId());
                key.setUserId(user.getUserId());
                if(reviewAgreeLogDOMapper.selectByPrimaryKey(key) != null)
                    temp.setHasAgree(true);
                else
                    temp.setHasAgree(false);
            }else{
                temp.setHasAgree(false);
            }
            ret.add(temp);
        }
        return ret;
    }

    public HashMap<String,String> delReview(Long reviewId) throws BusinessException{
        UserModel user = userService.getUserByToken();
        ReviewDO review = reviewDOMapper.selectByPrimaryKey(reviewId);
        String message;
        if(review == null || !review.getUserId().equals(user.getUserId())){
            System.out.println(review.getUserId() + "," + user.getUserId());
            throw new BusinessException(EmBusinessError.REVIEW_NOT_EXIST);
            //message = "Failed.";
        }else{
            reviewDOMapper.deleteByPrimaryKey(reviewId);
            message = "Success.";
        }
        HashMap<String,String> ret = new HashMap<String,String>();
        ret.put("message",message);
        return ret;
    }

    public HashMap<String,String> agreeReview(Long reviewId) throws BusinessException {
        ReviewAgreeLogDOKey key = new ReviewAgreeLogDOKey();
        UserModel user = userService.getUserByToken();
        key.setUserId(user.getUserId());
        key.setReviewId(reviewId);
        ReviewAgreeLogDO data = reviewAgreeLogDOMapper.selectByPrimaryKey(key);
        String message;
        //System.out.println("reviewID:" + reviewId);
        ReviewDO review = reviewDOMapper.selectByPrimaryKey(reviewId);//先把评论取出来，之后对赞同数++ --
        if(review == null){
            //System.out.println("reviewID:" + reviewId);
            throw new BusinessException(EmBusinessError.REVIEW_NOT_EXIST);
        }
        if(data == null){
            //没赞同过，赞同
            data = new ReviewAgreeLogDO();
            data.setReviewId(reviewId);
            data.setUserId(user.getUserId());
            reviewAgreeLogDOMapper.insertSelective(data);
            review.setAgreeNumber(review.getAgreeNumber() + 1);
            reviewDOMapper.updateByPrimaryKeySelective(review);
            message = "Agree success.";
        }else{
            //赞同了，取消
            reviewAgreeLogDOMapper.deleteByPrimaryKey(data);
            review.setAgreeNumber(Math.max(review.getAgreeNumber() - 1,0));
            reviewDOMapper.updateByPrimaryKeySelective(review);
            message = "Disagree success.";
        }
        HashMap<String,String> ret = new HashMap<String,String>();
        ret.put("message",message);
        return ret;
    }
}
