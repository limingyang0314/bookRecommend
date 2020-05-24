package com.example.springwebserver.service;


import com.example.springwebserver.dataObject.BookDO;
import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.service.model.BookModel;
import com.github.pagehelper.Page;
import com.mysql.cj.QueryResult;


import java.util.List;

public interface BookService {

    BookModel getBookById(Long book);

    BookModel getBookByIdInCache(Long book);

    BookModel createBook(BookModel bookModel) throws BusinessException;

    List<BookModel> listBookByPage(int page, int size);

    List<BookModel> listBookByAuthorPage(String author, int page, int size);

    List<BookModel> listBookByTagPage(String tagID, int page, int size);

    BookModel convertModelFromDO(BookDO bookDO);

}

