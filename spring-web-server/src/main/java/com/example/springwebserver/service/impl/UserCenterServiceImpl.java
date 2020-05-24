package com.example.springwebserver.service.impl;

import com.example.springwebserver.dao.UserHasReadDOMapper;
import com.example.springwebserver.dao.UserWantReadDOMapper;
import com.example.springwebserver.dataObject.UserHasReadDO;
import com.example.springwebserver.dataObject.UserWantReadDO;
import com.example.springwebserver.service.BookService;
import com.example.springwebserver.service.UserCenterService;
import com.example.springwebserver.service.UserService;
import com.example.springwebserver.service.model.BookModel;
import com.example.springwebserver.service.model.UserCenterModel;
import com.example.springwebserver.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserCenterServiceImpl implements UserCenterService {
    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserWantReadDOMapper userWantReadDOMapper;

    @Autowired
    private UserHasReadDOMapper userHasReadDOMapper;


    public UserCenterModel getUserCenterByUserID(Long userID){
        UserCenterModel ret = new UserCenterModel();
        UserModel user = userService.getUserById(userID);
        ret.setUser(user);
        List<String> wantID = this.getUserWantReadBookID(userID);
        List<BookModel> want,has;
        if(wantID != null) {
            want = bookService.listBookByBookIDSet(wantID);
        }else{
            want = new ArrayList<BookModel>();
        }
        List<String> hasID = this.getUserHasReadBookID(userID);
        if(hasID != null){
            has = bookService.listBookByBookIDSet(hasID);
        }else {
            has = new ArrayList<BookModel>();
        }
        ret.setWantRead(want);
        ret.setHasRead(has);
        return ret;
    }

    private List<String> getUserWantReadBookID(Long userID){
        UserWantReadDO DO = userWantReadDOMapper.selectByPrimaryKey(userID);
        if(DO == null){
            return null;
        }
        String ret = DO.getBooks();
        if(ret.isEmpty()){
            return null;
        }
        List<String> books = Arrays.asList(ret.split(","));
        return books;
    }

    private List<String> getUserHasReadBookID(Long userID){
        UserHasReadDO DO = userHasReadDOMapper.selectByPrimaryKey(userID);
        if(DO == null){
            return null;
        }
        String ret = DO.getBooks();
        if(ret.isEmpty()){
            return null;
        }
        List<String> books = Arrays.asList(ret.split(","));
        return books;
    }
}
