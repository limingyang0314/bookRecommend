package com.example.springwebserver.controller;


import com.example.springwebserver.controller.viewObject.AuthorShowVO;
import com.example.springwebserver.controller.viewObject.TagShowVO;
import com.example.springwebserver.dao.AuthorDOMapper;
import com.example.springwebserver.dao.BookDOMapper;
import com.example.springwebserver.dao.TagDOMapper;
import com.example.springwebserver.dataObject.AuthorDO;
import com.example.springwebserver.dataObject.BookDO;
import com.example.springwebserver.dataObject.TagDO;
import com.example.springwebserver.enums.EmBusinessError;
import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.response.CommonReturnType;
import com.example.springwebserver.service.BookService;
import com.example.springwebserver.service.RedisService;
import com.example.springwebserver.service.model.BookModel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller("rankList")
@RequestMapping("/rankList")
@Slf4j
@Api(tags = "年度榜单相关接口", value = "提供各种年度榜单的 Rest API")
public class RankListController {
    @Autowired
    private BookService bookService;

    @Autowired
    private TagDOMapper tagDOMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AuthorDOMapper authorDOMapper;

    @Autowired
    private BookDOMapper bookDOMapper;


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

    @ApiOperation("获取热门标签及其热书,每个标签最多返回8本")
    @GetMapping("/hotTagAndBook")
    @ResponseBody
    public CommonReturnType getHotTagAndBook(@RequestParam(name = "page") int page,
                                      @RequestParam(name = "size") int size) throws BusinessException {
        PageHelper.startPage(page, size);
        Page<TagDO> data = tagDOMapper.listTagByHotVal();
        List<TagDO> tags = data.getResult();
        List<TagShowVO> ret = new ArrayList<TagShowVO>();
        for(TagDO one : tags){
            TagShowVO temp;
//            System.out.println("Rest time :" + redisService.getExpire("Tag:" + one.getTagId()));
//            if(redisService.hasKey("Tag:" + one.getTagId()) &&  redisService.getExpire("Tag:" + one.getTagId()) > 0){// && redisService.getExpire("Tga:" + one.getTagId()) >= 0
//                System.out.println("Tag cache works:" + one.getTagId());
//                temp = (TagShowVO) redisService.get("Tag:" + one.getTagId());
//            }else{
                temp = new TagShowVO();
                BeanUtils.copyProperties(one,temp);
                List<BookModel> books = bookService.listBookByHotTag(one.getTagId().longValue());
                System.out.println(books.get(0).getBookName());
                temp.setHotBooks(books);
                redisTemplate.opsForValue().set("Tag:" +  one.getTagId(),temp,60, TimeUnit.MINUTES);
//            }
            ret.add(temp);
        }

        return CommonReturnType.create(ret);
    }

    @ApiOperation("获取热门作者及其热书,每个作者最多返回8本")
    @GetMapping("/hotAuthorAndBook")
    @ResponseBody
    public CommonReturnType getHotAuthorAndBook(@RequestParam(name = "page") int page,
                                             @RequestParam(name = "size") int size) throws BusinessException {
        PageHelper.startPage(page, size);
        Page<AuthorDO> data = authorDOMapper.listAuthorByHotVal();
        List<AuthorDO> authors = data.getResult();
        List<AuthorShowVO> ret = new ArrayList<AuthorShowVO>();
        for(AuthorDO one : authors){
            AuthorShowVO temp;
//            if(redisService.hasKey("Tag:" + one.getTagId()) && redisService.getExpire("Tag:" + one.getTagId()) >= 0){// && redisService.getExpire("Tga:" + one.getTagId()) >= 0
//                redisService.expire("Tag:" + one.getTagId(),3600);
//                System.out.println("Tag cache works:" + one.getTagId());
//                System.out.println(redisService.get("Tag:" + one.getTagId()));
//                temp = (TagShowVO) redisService.get("Tag:" + one.getTagId());
//            }else{
            temp = new AuthorShowVO();
            BeanUtils.copyProperties(one,temp);
            List<BookModel> books = bookService.listBookByHotAuthor(one.getAuthorId());
            System.out.println(one.getAuthorId() + " SIZE:" + books.size());
            temp.setHotBooks(books);
//                redisService.lSet("Tag:" + one.getTagId(),temp,36000);
//            }
            ret.add(temp);
        }

        return CommonReturnType.create(ret);
    }


    @ApiOperation("获取8本最热国内作品")
    @GetMapping("/hotChineseBook")
    @ResponseBody
    public CommonReturnType getHotChineseBook(){
        List<BookDO> books = bookDOMapper.listBookByChina();
        return CommonReturnType.create(books.stream().map(bookService::convertModelFromDO).collect(Collectors.toList()));
    }

    @ApiOperation("获取8本最热国外作品")
    @GetMapping("/hotBoardBook")
    @ResponseBody
    public CommonReturnType getHotBoardBook(){
        return CommonReturnType.create(bookDOMapper.listBookByBoard().stream().map(bookService::convertModelFromDO).collect(Collectors.toList()));
    }

}
