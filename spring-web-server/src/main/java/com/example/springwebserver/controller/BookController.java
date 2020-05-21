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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller("book")
@RequestMapping("/book")
@Slf4j
@Api(tags = "图书相关接口", value = "提供图书相关的 Rest API")
public class BookController extends GlobalExceptionHandler {

    @Autowired
    private BookService bookService;

    @GetMapping("/list")
    @ResponseBody
    public CommonReturnType getBookByPage(@RequestParam(name = "page") int page,
                                          @RequestParam(name = "size") int size) throws BusinessException {
        return CommonReturnType.create(bookService.listBookByPage(page, size));

    }
}
