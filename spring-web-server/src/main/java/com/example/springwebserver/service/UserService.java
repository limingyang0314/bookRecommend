package com.example.springwebserver.service;

import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.service.model.UserModel;


public interface UserService {

    UserModel getUserById(Long id);

    UserModel getUserByIdInCache(Long id);

    void register(UserModel userModel) throws BusinessException;

    UserModel validateLogin(String userName, String encryptPassword) throws BusinessException;
}
