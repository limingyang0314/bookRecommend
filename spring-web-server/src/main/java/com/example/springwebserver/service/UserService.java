package com.example.springwebserver.service;

import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.service.model.UserModel;

import java.util.HashMap;


public interface UserService {

    UserModel getUserById(Long id);

    UserModel getUserByIdInCache(Long id);

    void register(UserModel userModel) throws BusinessException;

    UserModel validateLogin(String userName, String encryptPassword) throws BusinessException;

    UserModel getUserByToken() throws BusinessException;

    HashMap<String,String> setWantRead(Long bookId) throws BusinessException;

    HashMap<String,String> setHasRead(Long bookId) throws BusinessException;

    boolean isLoginUser() throws BusinessException;
}
