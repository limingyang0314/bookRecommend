package com.example.springwebserver.service.impl;

import com.example.springwebserver.dao.BookDOMapper;
import com.example.springwebserver.dao.TagDOMapper;
import com.example.springwebserver.dataObject.BookDO;
import com.example.springwebserver.dataObject.TagDO;
import com.example.springwebserver.dataObject.UserDO;
import com.example.springwebserver.enums.EmBusinessError;
import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.service.BookService;
import com.example.springwebserver.service.RedisService;
import com.example.springwebserver.service.model.BookModel;
import com.example.springwebserver.validator.ValidatorImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDOMapper bookDOMapper;

    @Autowired
    private TagDOMapper tagDOMapper;

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private RedisTemplate redisTemplate;

    //@Autowired
    private BookDO bookDO;

    @Autowired
    private RedisService redisService;

    @Override
    public BookModel getBookById(Long book) {
        BookDO bookDO = bookDOMapper.selectByPrimaryKey(book);
        return convertModelFromDO(bookDO);
    }


    @Override
    public BookModel getBookByIdInCache(Long book) {
        if(redisTemplate.opsForValue().getOperations().getExpire("bookID:" + Long.toString(book)) < 0){
            //未缓存或已过期
            return getBookById(book);
        }
        return (BookModel)redisTemplate.opsForValue().get("bookID:" + book);
    }

    @Override
    public List<BookModel> listBookByAuthorPage(String authorID, int page, int size){
        PageHelper.startPage(page, size);
        Page<BookDO> bookPage = bookDOMapper.listBookByAuthorPage(authorID);
        List<BookDO> bookList = bookPage.getResult();

        return bookList.stream().map(this::convertModelFromDO).collect(Collectors.toList());
    }

    @Override
    public List<BookModel> listBookByTagPage(String tagID, int page, int size){
        PageHelper.startPage(page, size);
        Page<BookDO> bookPage = bookDOMapper.listBookByTagPage(tagID);
        List<BookDO> bookList = bookPage.getResult();

        return bookList.stream().map(this::convertModelFromDO).collect(Collectors.toList());
    }

    @Override
    public BookModel createBook(BookModel bookModel) throws BusinessException {
        return null;
    }

    @Override
    public List<BookModel> listBookByPage(int page, int size) {
        PageHelper.startPage(page, size);
        Page<BookDO> bookPage = bookDOMapper.listBookByPage();
        List<BookDO> bookList = bookPage.getResult();

        return bookList.stream().map(this::convertModelFromDO).collect(Collectors.toList());
    }

    public List<BookModel> listBookByBookIDSet(List<String> books, int page, int size) {
        PageHelper.startPage(page, size);
        Page<BookDO> bookPage = bookDOMapper.listBookByBookIDSetPage(books);
        List<BookDO> bookList = bookPage.getResult();

        return bookList.stream().map(this::convertModelFromDO).collect(Collectors.toList());
    }

    public List<BookModel> listBookByBookIDSet(List<String> books) {
        //Page<BookDO> bookPage = bookDOMapper.listBookByBookIDSetPage(books);
        List<BookDO> bookList = bookDOMapper.listBookByBookIDSet(books);
        return bookList.stream().map(this::convertModelFromDO).collect(Collectors.toList());
    }

    public BookModel convertModelFromDO(BookDO bookDO) {
        this.bookDO = bookDO;
        //缓存中有则直接获取
//        if(redisService.getExpire("bookID:" + bookDO.getBookId()) >= 0)
//            return (BookModel) redisService.get("bookID:" + bookDO.getBookId());
        BookModel bookModel = new BookModel();
        BeanUtils.copyProperties(bookDO, bookModel);
        bookModel.setPrice(BigDecimal.valueOf(bookDO.getPrice()));
        if (!StringUtils.isEmpty(bookDO.getTagIds())) {
            String[] tagIds = bookDO.getTagIds().split(",");
            bookModel.setTagIds(Arrays.asList(tagIds));
            List<TagDO> tags = tagDOMapper.listTagByTagIDs(bookModel.getTagIds());
            List<String> tagsString = new ArrayList<String>();
            System.out.println(tags);
            for(TagDO t : tags){
                tagsString.add(t.getTagName());
            }
            System.out.println(tagsString);
            bookModel.setTags(tagsString);
        }

        if (!StringUtils.isEmpty(bookDO.getSellerlist())) {
            String[] sellers = bookDO.getSellerlist().split(";");
            List<BookModel.Seller> sellerList = new ArrayList<>();
            for (String s : sellers) {
                if (StringUtils.isEmpty(s)) {
                    continue;
                }
                String[] temp = s.split(",");
                BookModel.Seller seller;
                if (StringUtils.equals(temp[1].trim(), "None")) {
                    continue;
                }
                try {
                    seller = new BookModel.Seller(temp[0], new BigDecimal(temp[1].trim()));
                } catch (NumberFormatException e) {
                    System.out.println(bookDO.getSellerlist());
                    continue;
                }

                sellerList.add(seller);
            }
            //String tagIDs = bookModel.getTagIds();

            bookModel.setSellerlist(sellerList);
            if(redisService.getExpire("bookID:" + bookDO.getBookId()) < 0)
                redisTemplate.opsForValue().set("bookID:" + bookModel.getBookId(),bookModel,100000);
        }

        return bookModel;
    }


}
