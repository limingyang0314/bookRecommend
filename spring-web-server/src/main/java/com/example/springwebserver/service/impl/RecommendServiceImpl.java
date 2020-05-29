package com.example.springwebserver.service.impl;

import com.example.springwebserver.dao.BookDOMapper;
import com.example.springwebserver.dao.BookRecommendDOMapper;
import com.example.springwebserver.dao.TagDOMapper;
import com.example.springwebserver.dao.UserRecommendDOMapper;
import com.example.springwebserver.dataObject.BookDO;
import com.example.springwebserver.dataObject.BookRecommendDO;
import com.example.springwebserver.dataObject.TagDO;
import com.example.springwebserver.dataObject.UserRecommendDO;
import com.example.springwebserver.service.RecommendService;
import com.example.springwebserver.service.BookService;
import com.example.springwebserver.service.model.BookModel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendServiceImpl implements RecommendService {
    @Autowired
    private BookRecommendDOMapper bookRecommendDOMapper;

    @Autowired
    private UserRecommendDOMapper userRecommendDOMapper;

    @Autowired
    private BookDOMapper bookDOMapper;

    @Autowired
    private BookService bookService;

    @Autowired
    private TagDOMapper tagDOMapper;

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
        List<String> books = Arrays.asList(recommends.split(","));
        PageHelper.startPage(page, size);
        Page<BookDO> bookPage = bookDOMapper.listBookByBookIDSetPage(books);
        List<BookDO> bookList = bookPage.getResult();

        return bookList.stream().map(bookService::convertModelFromDO).collect(Collectors.toList());
    }

    /**
     * 根据userID获取相关推荐图书
     * @param userID
     * @param page
     * @param size
     * @return
     */
    @Override
    public List<BookModel> listBookRecommendByUserID(Long userID, int page, int size){
        UserRecommendDO userRecommend = userRecommendDOMapper.selectByPrimaryKey(userID);
        //获取推荐的id集合
        String recommends = userRecommend.getBooks();
        System.out.println(recommends);
        List<String> books = Arrays.asList(recommends.split(","));
        PageHelper.startPage(page, size);
        Page<BookDO> bookPage = bookDOMapper.listBookByBookIDSetPage(books);
        List<BookDO> bookList = bookPage.getResult();

        return bookList.stream().map(bookService::convertModelFromDO).collect(Collectors.toList());
    }

    @Override
    public List<TagDO> listTagRecommendByUserID(Long userID){
        UserRecommendDO userRecommend = userRecommendDOMapper.selectByPrimaryKey(userID);
        //UserRecommendDO userRecommend = userRecommendDOMapper.selectByPrimaryKey(userID);
        String recommends = userRecommend.getTags();
        System.out.println(recommends);
        if(recommends == "" || recommends == null){
            return new ArrayList<TagDO>();
        }
        List<String> tags = Arrays.asList(recommends.split(","));
        List<TagDO> tagList = tagDOMapper.listTagByTagIDs(tags);

        return tagList;

    }
}
