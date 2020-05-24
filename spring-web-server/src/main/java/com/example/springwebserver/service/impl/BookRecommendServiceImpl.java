package com.example.springwebserver.service.impl;

import com.example.springwebserver.dao.BookDOMapper;
import com.example.springwebserver.dao.BookRecommendDOMapper;
import com.example.springwebserver.dataObject.BookDO;
import com.example.springwebserver.dataObject.BookRecommendDO;
import com.example.springwebserver.service.BookRecommendService;
import com.example.springwebserver.service.BookService;
import com.example.springwebserver.service.model.BookModel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookRecommendServiceImpl implements BookRecommendService{
    @Autowired
    private BookRecommendDOMapper bookRecommendDOMapper;

    @Autowired
    private BookDOMapper bookDOMapper;

    @Autowired
    private BookService bookService;

    /**
     * 根据一本图书推荐相关图书
     * @param bookID
     * @param page
     * @param size
     * @return
     */
    @Override
    public List<BookModel> listBookRecommendByBookID(Long bookID, int page, int size){
        BookRecommendDO bookRecommend = bookRecommendDOMapper.selectByPrimaryKey(bookID);
        //获取推荐的id集合
        String recommends = bookRecommend.getBooks();
        System.out.println(recommends);
        PageHelper.startPage(page, size);
        Page<BookDO> bookPage = bookDOMapper.listBookByBookIDSet(recommends);
        List<BookDO> bookList = bookPage.getResult();

        return bookList.stream().map(bookService::convertModelFromDO).collect(Collectors.toList());
    }
}
