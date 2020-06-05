package com.example.springwebserver.service;


import com.example.springwebserver.dataObject.BookDO;
import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.response.CommonReturnType;
import com.example.springwebserver.service.model.BookModel;
import com.github.pagehelper.Page;
import com.mysql.cj.QueryResult;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public interface BookService {

    BookModel getBookById(Long book);

    BookModel getBookByIdInCache(Long book);

    BookModel createBook(BookModel bookModel) throws BusinessException;

    List<BookModel> listBookByPage(int page, int size);

    List<BookModel> listBookByAuthorPage(String author, int page, int size);

    List<BookModel> listBookByTagPage(String tagID, int page, int size);

    List<BookModel> listBookByBookIDSet(List<String> books, int page, int size);

    List<BookModel> listBookByBookIDSet(List<String> books);

    List<BookModel> listBookByHotRank(int page, int size);

    HashMap<String,String> ratingBook(Long bookId, double ratingNum) throws BusinessException;

    boolean isWantRead(Long userId,Long bookId) throws BusinessException;

    boolean isHasRead(Long userId,Long bookId) throws BusinessException;

    BookModel convertModelFromDO(BookDO bookDO);

    List<BookModel> listBookByHotTag(Long tagID);

    List<BookModel> listBookByHotAuthor(Long authorID);

}

