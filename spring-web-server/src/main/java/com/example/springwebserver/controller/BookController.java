package com.example.springwebserver.controller;

import com.example.springwebserver.enums.EmBusinessError;
import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.response.CommonReturnType;
import com.example.springwebserver.service.BookService;
import com.example.springwebserver.service.model.BookModel;
import com.mysql.cj.QueryResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation("分页获取书籍")
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
    @ApiOperation("根据作者id分页获取书籍")
    @GetMapping("/author")
    @ResponseBody
    public CommonReturnType getBookByAuthorPage(@RequestParam(name = "page") int page,
                                                @RequestParam(name = "size") int size, @RequestParam(name = "author_ID") String author_ID) throws BusinessException {
        return CommonReturnType.create(bookService.listBookByAuthorPage(author_ID, page, size));

    }

    /**
     * 根据标签ID分页获取书籍
     * @param page
     * @param size
     * @param tag_ID
     * @return
     * @throws BusinessException
     */
    @ApiOperation("根据标签ID分页获取书籍")
    @GetMapping("/tag")
    @ResponseBody
    public CommonReturnType getBookByTagPage(@RequestParam(name = "page") int page,
                                             @RequestParam(name = "size") int size, @RequestParam(name = "tag_ID") String tag_ID) throws BusinessException {
        List<BookModel> data = bookService.listBookByTagPage(tag_ID,page,size);
        return CommonReturnType.create(data);

    }

    /**
     * 根据图书ID获取书籍
     * @param book_ID
     * @return
     * @throws BusinessException
     */
    @ApiOperation("根据图书ID获取书籍")
    @GetMapping("")
    @ResponseBody
<<<<<<< HEAD
    public CommonReturnType getBookByBookID(@RequestParam(name = "bookId") long bookId) throws BusinessException {
        BookModel data = bookService.getBookById(bookId);
=======
    public CommonReturnType getBookByBookID(@RequestParam(name = "book_ID") long book_ID) throws BusinessException {
        BookModel data = bookService.getBookById(book_ID);
>>>>>>> d06ea346bd31022e4419193920aaacf914ad9e06
        if(data == null){
            log.warn("==== [get book] ==== book not exit");
            throw new BusinessException(EmBusinessError.BOOK_NOT_EXIST);
        }
        return CommonReturnType.create(data);
    }




}
