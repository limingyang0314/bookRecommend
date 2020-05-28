package com.example.springwebserver.service.impl;

import com.example.springwebserver.dao.UserDOMapper;
import com.example.springwebserver.dao.UserHasReadDOMapper;
import com.example.springwebserver.dao.UserPasswordDOMapper;
import com.example.springwebserver.dao.UserWantReadDOMapper;
import com.example.springwebserver.dataObject.UserDO;
import com.example.springwebserver.dataObject.UserHasReadDO;
import com.example.springwebserver.dataObject.UserPasswordDO;
import com.example.springwebserver.dataObject.UserWantReadDO;
import com.example.springwebserver.enums.EmBusinessError;
import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.service.UserService;
import com.example.springwebserver.service.model.UserModel;
import com.example.springwebserver.validator.ValidationResult;
import com.example.springwebserver.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private UserWantReadDOMapper userWantReadDOMapper;

    @Autowired
    private UserHasReadDOMapper userHasReadDOMapper;


    @Override
    public UserModel getUserById(Long id) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if (userDO == null) {
            return null;
        }
        // get password by user id
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByPrimaryKey(userDO.getUserId());

        return convertFromDataObject(userDO, userPasswordDO);
    }


    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO) {
        if (userDO == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO, userModel);

        if (userPasswordDO != null) {
            userModel.setEncryptPassword(userPasswordDO.getPassword());
        }

        return userModel;
    }

    @Override
    public UserModel getUserByIdInCache(Long id) {
        UserModel userModel = (UserModel) redisTemplate.opsForValue().get("user_validate_" + id);
        if (userModel == null) {
            userModel = this.getUserById(id);
            redisTemplate.opsForValue().set("user_validate_" + id, userModel);
            redisTemplate.expire("user_validate_" + id,10, TimeUnit.MINUTES);
        }
        return userModel;
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        ValidationResult result =  validator.validate(userModel);
        if (result.isHasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }
        // model -> dataObject
        UserDO userDO = convertFromModel(userModel);
        try {
            userDOMapper.insertSelective(userDO);
        } catch (DuplicateKeyException ex) {
            throw new BusinessException(EmBusinessError.USER_ALREADY_REGISTR);
        }
        userModel.setUserId(userDO.getUserId());
        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);
    }

    private UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if (userModel == null) {
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setPassword(userModel.getEncryptPassword());
        userPasswordDO.setUserId(userModel.getUserId());
        return userPasswordDO;
    }

    private UserDO convertFromModel(UserModel userModel){
        if (userModel == null) {
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel, userDO);

        return userDO;
    }

    @Override
    public UserModel validateLogin(String userName, String encryptPassword) throws BusinessException {
        UserDO userDO = userDOMapper.selectByUserName(userName);
        if (userDO == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByPrimaryKey(userDO.getUserId());
        UserModel userModel = convertFromDataObject(userDO, userPasswordDO);

        if (!StringUtils.equals(encryptPassword, userModel.getEncryptPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    @Override
    public UserModel getUserByToken() throws BusinessException{

        String token = httpServletRequest.getHeader("Authorization");
        System.out.println("token :" +token);
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN, "illegal user");
        }

        UserModel userModel = (UserModel) redisTemplate.opsForValue().get(token);
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        return userModel;
    }

    @Override
    public boolean isLoginUser() throws BusinessException{
        String token = httpServletRequest.getHeader("Authorization");
        if(token == null){
            return false;
        }
        UserModel user = (UserModel) redisTemplate.opsForValue().get(token);
        if(user == null){
            return false;
        }
        return true;
    }

    @Override
    public HashMap<String,String> setWantRead(Long bookId) throws BusinessException{
        HashMap<String,String> ret = new HashMap<String,String>();
        UserModel user = getUserByToken();
        Long userID = user.getUserId();
        UserWantReadDO want = userWantReadDOMapper.selectByPrimaryKey(userID);
        List<String> wantList = Arrays.asList(want.getBooks().split(","));
        String newWant = "";
        boolean found = false;
        String message;
        for(String s : wantList){
            if(s.equals("")){
                continue;
            }
            if(!s.equals(Long.toString(bookId))){
                newWant += s + ",";
            }else{
                found = true;
            }
        }
        if(found){
            //说明之前就有，要取消
            message = "Delete success.";
            if(newWant.length() > 0)
                newWant = newWant.substring(0, newWant.length() - 1);
        }else{
            //加上
            message = "Add success.";
            newWant += Long.toString(bookId);
        }
        ret.put("message",message);
        want.setBooks(newWant);
        userWantReadDOMapper.updateByPrimaryKey(want);
        return ret;
    }

    @Override
    public HashMap<String,String> setHasRead(Long bookId) throws BusinessException{
        HashMap<String,String> ret = new HashMap<String,String>();
        UserModel user = getUserByToken();
        Long userID = user.getUserId();
        UserHasReadDO has = userHasReadDOMapper.selectByPrimaryKey(userID);
        List<String> hasList = Arrays.asList(has.getBooks().split(","));
        String newHas = "";
        boolean found = false;
        String message;
        for(String s : hasList){
            if(s.equals("")){
                continue;
            }
            if(!s.equals(Long.toString(bookId))){
                newHas += s + ",";
            }else{
                found = true;
            }
        }
        if(found){
            //说明之前就有，要取消
            message = "Delete success.";
            if(newHas.length() > 0)
                newHas = newHas.substring(0, newHas.length() - 1);
        }else{
            //加上
            message = "Add success.";
            newHas += Long.toString(bookId);
        }
        ret.put("message",message);
        has.setBooks(newHas);
        userHasReadDOMapper.updateByPrimaryKey(has);
        return ret;
    }
}
