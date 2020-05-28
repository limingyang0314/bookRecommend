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
        HashMap<String,String> ret = reviewService.delReview(reviewId);
        return CommonReturnType.create(ret);
    }

    @ApiOperation("赞同/取消赞同一条评论")
    @PostMapping("agree")
    @ResponseBody
    public CommonReturnType agreeReview(@RequestParam(name = "reviewId") Long reviewId) throws BusinessException {
        HashMap<String,String> ret = reviewService.agreeReview(reviewId);
        return CommonReturnType.create(ret);
    }

    @ApiOperation("分页获取某图书的书评(赞同人数降序)")
    @PostMapping("book/agreeNum")
    @ResponseBody
    public CommonReturnType getReviewByBookIdDescAgreeNum(@RequestParam(name = "bookId") Long bookId,
                                              @RequestParam(name = "page") int page,
                                              @RequestParam(name = "size") int size) throws BusinessException {
        List<ReviewVO> ret = reviewService.getReviewByBookIdDescByAgreeNum(bookId,page,size);
        return CommonReturnType.create(ret);
    }

    @ApiOperation("分页获取某图书的书评(评论时间降序)")
    @PostMapping("book/reviewTime")
    @ResponseBody
    public CommonReturnType getReviewByBookIdDescByTime(@RequestParam(name = "bookId") Long bookId,
                                              @RequestParam(name = "page") int page,
                                              @RequestParam(name = "size") int size) throws BusinessException {
        List<ReviewVO> ret = reviewService.getReviewByBookIdDescByTime(bookId,page,size);
        return CommonReturnType.create(ret);
    }


}
