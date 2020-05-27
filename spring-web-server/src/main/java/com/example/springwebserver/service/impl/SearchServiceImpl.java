package com.example.springwebserver.service.impl;

import com.example.springwebserver.dao.BookDOMapper;
import com.example.springwebserver.dataObject.BookDO;
import com.example.springwebserver.service.BookService;
import com.example.springwebserver.service.SearchService;
import com.example.springwebserver.service.model.BookModel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("SearchService")
public class SearchServiceImpl implements SearchService {
    @Autowired
    private BookDOMapper bookDOMapper;

    @Autowired
    private BookService bookService;

    @Override
    public List<BookModel> searchByKey(String key, int page, int size){
        List<String> keys = Arrays.asList(key.split(","));
        String newKey = "%";
        for(String s : keys){
            if(s.length() == 13){
                char[] temp = s.toCharArray();
                if(temp[0] == '9' && temp[1] == '7') {
                    //很有可能是isbn
                    newKey = s;
                    break;
                }
            }
            newKey += s + "%";
        }
        PageHelper.startPage(page, size);
        Page<BookDO> bookPage = bookDOMapper.listBookBySearchKey(newKey);
        List<BookDO> bookList = bookPage.getResult();
        return bookList.stream().map(bookService::convertModelFromDO).collect(Collectors.toList());
    }
}
