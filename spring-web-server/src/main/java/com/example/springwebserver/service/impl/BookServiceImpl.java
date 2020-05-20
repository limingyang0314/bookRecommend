package com.example.springwebserver.service.impl;

import com.example.springwebserver.dao.BookDOMapper;
import com.example.springwebserver.dataObject.BookDO;
import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.service.BookService;
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
    private ValidatorImpl validator;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public BookModel getBookById(Long book) {
        return null;
    }

    @Override
    public BookModel getBookByIdInCache(Long book) {
        return null;
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

    private BookModel convertModelFromDO(BookDO bookDO) {
        BookModel bookModel = new BookModel();
        BeanUtils.copyProperties(bookDO, bookModel);
        bookModel.setPrice(BigDecimal.valueOf(bookDO.getPrice()));
        if (!StringUtils.isEmpty(bookDO.getTagIds())) {
            String[] tagIds = bookDO.getTagIds().split(",");
            bookModel.setTagIds(Arrays.asList(tagIds));
        }

        if (!StringUtils.isEmpty(bookDO.getSellerlist())) {
            String[] sellers = bookDO.getSellerlist().split(";");
            List<BookModel.Seller> sellerList = new ArrayList<>();
            for (String s : sellers) {
                if (StringUtils.isEmpty(s)) {
                    continue;
                }
                String[] temp = s.split(",");
                BookModel.Seller seller = new BookModel.Seller(temp[0], new BigDecimal(temp[1]));
                sellerList.add(seller);
            }
            bookModel.setSellerlist(sellerList);
        }

        return bookModel;
    }


}
