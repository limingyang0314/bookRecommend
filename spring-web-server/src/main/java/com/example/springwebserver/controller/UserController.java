package com.example.springwebserver.controller;

import com.example.springwebserver.controller.viewObject.UserVO;
import com.example.springwebserver.enums.EmBusinessError;
import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.response.CommonReturnType;
import com.example.springwebserver.service.UserService;
import com.example.springwebserver.service.model.UserModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller("user")
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户相关接口", value = "提供用户相关的 Rest API")
public class UserController extends GlobalExceptionHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;


    @ApiOperation("根据 id 获取用户")
    @GetMapping("")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Long id) throws BusinessException {
        UserModel userModel = userService.getUserByIdInCache(id);
        if (userModel == null) {
            log.warn("==== [get user] ==== user not exit");
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        UserVO userVO = convertFromModel(userModel);
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }

    @ApiOperation("用户注册接口")
    @PostMapping(value = "/register")
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "userName") String userName,
                                     @RequestParam(name = "gender") Boolean gender,
                                     @RequestParam(name = "age") Integer age,
                                     @RequestParam(name = "password") String password,
                                     @RequestParam(name = "introduction", required = false) String introduction
    ) throws BusinessException {

        UserModel userModel = new UserModel();
        userModel.setUserName(userName);
        userModel.setGender(gender);
        userModel.setAge(age);
        userModel.setIntroduction(introduction);
        try {
            userModel.setEncryptPassword(this.EncodeByMd5(password));
        } catch (NoSuchAlgorithmException e) {
            log.error("=== convert to md5 fail ====: {}", e.getMessage());
        }

        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    public String EncodeByMd5(String str) throws NoSuchAlgorithmException {

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        return base64en.encode(md5.digest(str.getBytes(StandardCharsets.UTF_8)));
    }

    @ApiOperation("用户登录接口")
    @PostMapping(value = "/login")
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "userName") String userName,
                                  @RequestParam(name = "password") String password) throws BusinessException, NoSuchAlgorithmException {

        if (StringUtils.isEmpty(userName)
                || StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FORM_CONTENT_BLACK);
        }

        UserModel userModel = userService.validateLogin(userName, EncodeByMd5(password));

        String uuidToken = UUID.randomUUID().toString();
        uuidToken = uuidToken.replace("-", "");

        redisTemplate.opsForValue().set(uuidToken, userModel);
        redisTemplate.expire(uuidToken, 1, TimeUnit.HOURS);
        return CommonReturnType.create(uuidToken);
    }

}
