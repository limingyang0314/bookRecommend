package com.example.springwebserver.controller;


import com.example.springwebserver.dao.TagDOMapper;
import com.example.springwebserver.dataObject.TagDO;
import com.example.springwebserver.enums.EmBusinessError;
import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.response.CommonReturnType;
import com.example.springwebserver.service.RecommendService;
import com.example.springwebserver.service.BookService;
import com.example.springwebserver.service.model.BookModel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller("recommend")
@RequestMapping("/recommend")
@Slf4j
@Api(tags = "推荐相关接口", value = "提供个性化推荐相关的 Rest API")
public class RecommendController {
    @Autowired
    private RecommendService recommendService;

    @Autowired
    private BookService bookService;

    @Autowired
    private TagDOMapper tagDOMapper;

    @GetMapping("/testToken")
    @ResponseBody
    public CommonReturnType testToken() throws BusinessException {

        return CommonReturnType.create(null);

    }

    /**
     * 通过bookID获取相关推荐
     * @param page, size, bookID
     * @return
     * @throws BusinessException
     */
    @ApiOperation("获取某本书的相关书籍推荐")
    @GetMapping("/book")
    @ResponseBody
    public CommonReturnType getRecommendByBookID(@RequestParam(name = "page") int page,
                                                 @RequestParam(name = "size") int size,
                                                 @RequestParam(name = "bookID") Long bookID) throws BusinessException {
        List<BookModel> data = recommendService.listBookRecommendByBookID(bookID,page,size);
        if(data.size() == 0){
            throw new BusinessException(EmBusinessError.BOOK_NOT_EXIST);
        }
        return CommonReturnType.create(data);
    }

    /**
     * 通过userID获取相关推荐
     * @param page
     * @param size
     * @param userID
     * @return
     * @throws BusinessException
     */
    @ApiOperation("获取某用户的相关书籍推荐")
    @GetMapping("/user")
    @ResponseBody
    public CommonReturnType getRecommendByUserID(@RequestParam(name = "page") int page,
                                                 @RequestParam(name = "size") int size,
                                                 @RequestParam(name = "userID") Long userID) throws BusinessException {
        List<BookModel> data = recommendService.listBookRecommendByUserID(userID,page,size);
        if(data.size() == 0){
            throw new BusinessException(EmBusinessError.BOOK_NOT_EXIST);
        }
        return CommonReturnType.create(data);
    }

    @ApiOperation("获取热书榜")
    @GetMapping("/hotRank")
    @ResponseBody
    public CommonReturnType getHotRank(@RequestParam(name = "page") int page,
                                                 @RequestParam(name = "size") int size) throws BusinessException {
        List<BookModel> data = bookService.listBookByHotRank(page,size);
        if(data.size() == 0){
            throw new BusinessException(EmBusinessError.BOOK_NOT_EXIST);
        }
        return CommonReturnType.create(data);
    }

    @ApiOperation("获取某用户的相关标签推荐")
    @GetMapping("/userTag")
    @ResponseBody
    public CommonReturnType getRecommendTagByUserID(@RequestParam(name = "userID") Long userID) throws BusinessException {
        List<TagDO> data = recommendService.listTagRecommendByUserID(userID);
//        if(data.size() == 0){
//            throw new BusinessException(EmBusinessError.TAG_NOT_EXIST);
//        }
        return CommonReturnType.create(data);
    }

    @ApiOperation("获取热门标签")
    @GetMapping("/hotTag")
    @ResponseBody
    public CommonReturnType getHotTag(@RequestParam(name = "page") int page,
                                       @RequestParam(name = "size") int size) throws BusinessException {
        PageHelper.startPage(page, size);
        Page<TagDO> data = tagDOMapper.listTagByHotVal();
        List<TagDO> ret = data.getResult();
        return CommonReturnType.create(ret);
    }
}
