package com.example.springwebserver.controller;

import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.response.CommonReturnType;
import com.example.springwebserver.service.BookService;
import com.example.springwebserver.service.model.BookModel;
import com.mysql.cj.QueryResult;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller("book")
@RequestMapping("/book")
@Slf4j
@Api(tags = "图书相关接口", value = "提供图书相关的 Rest API")
public class BookController extends GlobalExceptionHandler {

    @Autowired
    private BookService bookService;

    /**
     * 分页获取书籍
     * @param page
     * @param size
     * @return
     * @throws BusinessException
     */
    @GetMapping("/list")
    @ResponseBody
    public CommonReturnType getBookByPage(@RequestParam(name = "page") int page,
                                          @RequestParam(name = "size") int size) throws BusinessException {
        return CommonReturnType.create(bookService.listBookByPage(page, size));

    }

    /**
     * 根据作者id分页获取书籍
     * @param page
     * @param size
     * @param author_ID
     * @return
     * @throws BusinessException
     */
    @GetMapping("/author/{author_ID}")
    @ResponseBody
    public CommonReturnType getBookByAuthorPage(@RequestParam(name = "page") int page,
                                                @RequestParam(name = "size") int size, @PathVariable String author_ID) throws BusinessException {
        return CommonReturnType.create(bookService.listBookByPage(page, size));

    }

    /**
     * 根据标签ID分页获取书籍
     * @param page
     * @param size
     * @param tag_ID
     * @return
     * @throws BusinessException
     */
    @GetMapping("/tag/{tag_ID}")
    @ResponseBody
    public CommonReturnType getBookByTagPage(@RequestParam(name = "page") int page,
                                             @RequestParam(name = "size") int size, @PathVariable String tag_ID) throws BusinessException {
        return CommonReturnType.create(bookService.listBookByPage(page, size));

    }

    @GetMapping("/{book_ID}")
    @ResponseBody
    public CommonReturnType getBookByBookID(@PathVariable String book_ID) throws BusinessException {
        return null;

    }




}
