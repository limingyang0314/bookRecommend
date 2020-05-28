package com.example.springwebserver.controller;

import com.example.springwebserver.controller.viewObject.ReviewVO;
import com.example.springwebserver.dao.BookDOMapper;
import com.example.springwebserver.dao.ReviewAgreeLogDOMapper;
import com.example.springwebserver.dao.ReviewDOMapper;
import com.example.springwebserver.dataObject.BookDO;
import com.example.springwebserver.dataObject.ReviewAgreeLogDO;
import com.example.springwebserver.dataObject.ReviewAgreeLogDOKey;
import com.example.springwebserver.dataObject.ReviewDO;
import com.example.springwebserver.enums.EmBusinessError;
import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.response.CommonReturnType;
import com.example.springwebserver.service.ReviewService;
import com.example.springwebserver.service.SearchService;
import com.example.springwebserver.service.UserService;
import com.example.springwebserver.service.model.UserModel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller("review")
@RequestMapping("/review")
@Slf4j
@Api(tags = "评论相关接口", value = "提供评论增删查的 Rest API")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewDOMapper reviewDOMapper;

    @Autowired
    private ReviewAgreeLogDOMapper reviewAgreeLogDOMapper;

    @Autowired
    private BookDOMapper bookDOMapper;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @ApiOperation("添加一条评论")
    @PostMapping("add")
    @ResponseBody
    public CommonReturnType addReview(@RequestParam(name = "bookId") Long bookId,
                                      @RequestParam(name = "content") String content,
                                      @RequestParam(name = "star") int star) throws BusinessException {
        HashMap<String,String> ret = reviewService.addReview(bookId,content,star);
        return CommonReturnType.create(ret);
    }

    @ApiOperation("删除一条评论")
    @PostMapping("delete")
    @ResponseBody
    public CommonReturnType delReview(@RequestParam(name = "reviewId") Long reviewId) throws BusinessException {
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
        return CommonReturnType.create(ret);
    }

    @ApiOperation("赞同/取消赞同一条评论")
    @PostMapping("agree")
    @ResponseBody
    public CommonReturnType agreeReview(@RequestParam(name = "reviewId") Long reviewId) throws BusinessException {
        //System.out.println("reviewID:" + reviewId);
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
        return CommonReturnType.create(ret);
    }

    @ApiOperation("分页获取某图书的书评(赞同人数降序)")
    @PostMapping("book/agreeNum")
    @ResponseBody
    public CommonReturnType getReviewByBookIdDescAgreeNum(@RequestParam(name = "bookId") Long bookId,
                                              @RequestParam(name = "page") int page,
                                              @RequestParam(name = "size") int size) throws BusinessException {
        PageHelper.startPage(page, size);
        Page<ReviewDO> reviewPage = reviewDOMapper.listReviewByBookIDDescByAgreeNum(bookId);
        List<ReviewDO> reviewList = reviewPage.getResult();

        List<ReviewVO> ret = new ArrayList<ReviewVO>();

        boolean login = userService.isLoginUser();
        if(login){
            UserModel user = userService.getUserByToken();
        }
        for(ReviewDO one: reviewList){
            ReviewVO temp = new ReviewVO();
            BeanUtils.copyProperties(one,temp);
            if(login){
                temp.setHasAgree();
                //System.out.println("fuck");
            }
            ret.add(temp);
        }
        return CommonReturnType.create(reviewList);
    }

    @ApiOperation("分页获取某图书的书评(评论时间降序)")
    @PostMapping("book/reviewTime")
    @ResponseBody
    public CommonReturnType getReviewByBookIdDescByTime(@RequestParam(name = "bookId") Long bookId,
                                              @RequestParam(name = "page") int page,
                                              @RequestParam(name = "size") int size) throws BusinessException {
        PageHelper.startPage(page, size);
        Page<ReviewDO> reviewPage = reviewDOMapper.listReviewByBookIDDescByAgreeNum(bookId);
        List<ReviewDO> reviewList = reviewPage.getResult();

        List<ReviewVO> ret = new ArrayList<ReviewVO>();

        boolean login = userService.isLoginUser();
        if(login){
            UserModel user = userService.getUserByToken();
        }
        for(ReviewDO one: reviewList){
            ReviewVO temp = new ReviewVO();
            BeanUtils.copyProperties(one,temp);
            if(login){
                temp.setHasAgree();
                //System.out.println("fuck");
            }
            ret.add(temp);
        }
        return CommonReturnType.create(reviewList);
    }


}
