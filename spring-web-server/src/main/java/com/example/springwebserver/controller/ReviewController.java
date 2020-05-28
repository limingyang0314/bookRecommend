package com.example.springwebserver.controller;

import com.example.springwebserver.dao.ReviewDOMapper;
import com.example.springwebserver.dataObject.ReviewDO;
import com.example.springwebserver.enums.EmBusinessError;
import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.response.CommonReturnType;
import com.example.springwebserver.service.ReviewService;
import com.example.springwebserver.service.SearchService;
import com.example.springwebserver.service.UserService;
import com.example.springwebserver.service.model.UserModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

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
        if(review == null || review.getUserId() != user.getUserId()){
            throw new BusinessException(EmBusinessError.REVIEW_NOT_EXIST);
            //message = "Failed.";
        }else{
            reviewDOMapper.deleteByPrimaryKey(reviewId);
            message = "Success.";
        }
        HashMap<String,String> ret = new HashMap<String,String>();
        ret.put("message",message);
    }

    @ApiOperation("赞同/取消赞同一条评论")
    @PostMapping("add")
    @ResponseBody
    public CommonReturnType agreeReview(@RequestParam(name = "bookId") Long bookId){
        return null;
    }

    @ApiOperation("分页获取某图书的书评")
    @PostMapping("book")
    @ResponseBody
    public CommonReturnType getReviewByBookId(@RequestParam(name = "bookId") Long bookId,
                                              @RequestParam(name = "page") int page,
                                              @RequestParam(name = "size") int size){

    }


}
