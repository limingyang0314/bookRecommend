package com.example.springwebserver.controller;

import com.example.springwebserver.controller.viewObject.UserVO;
import com.example.springwebserver.dao.UserHasReadDOMapper;
import com.example.springwebserver.dao.UserWantReadDOMapper;
import com.example.springwebserver.dataObject.UserHasReadDO;
import com.example.springwebserver.dataObject.UserWantReadDO;
import com.example.springwebserver.enums.EmBusinessError;
import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.response.CommonReturnType;
import com.example.springwebserver.service.UserCenterService;
import com.example.springwebserver.service.UserService;
import com.example.springwebserver.service.model.UserCenterModel;
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

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller("user")
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户相关接口", value = "提供用户相关的 Rest API")
public class UserController extends GlobalExceptionHandler {
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserCenterService userCenterService;

    @Autowired
    private UserWantReadDOMapper userWantReadDOMapper;

    @Autowired
    private UserHasReadDOMapper userHasReadDOMapper;


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
        userModel.setEncryptPassword(null);

        Map<String, Object> map = new HashMap<>();
        map.put("token", uuidToken);
        map.put("user", userModel);

        return CommonReturnType.create(map);
    }

    /**
     * 获取用户个人中心的数据
     * @return
     */
    @ApiOperation("用户中心数据")
    @GetMapping(value = "/center")
    @ResponseBody
    public CommonReturnType userCenter(@RequestParam(name = "userID") long userID){
        //String token = httpServletRequest.getHeader("Authorization");
        UserCenterModel ret = userCenterService.getUserCenterByUserID(userID);
        return CommonReturnType.create(ret);
    }

    @ApiOperation("设为想读/取消想读")
    @GetMapping(value = "/wantRead")
    @ResponseBody
    public CommonReturnType setWantRead(@RequestParam(name = "bookID") long bookID) throws BusinessException{
        HashMap<String,String> ret = userService.setWantRead(bookID);
        return CommonReturnType.create(ret);
    }

    @ApiOperation("设为已读/取消已读")
    @GetMapping(value = "/hasRead")
    @ResponseBody
    public CommonReturnType setHasRead(@RequestParam(name = "bookID") long bookID) throws BusinessException{
        HashMap<String,String> ret = userService.setHasRead(bookID);
        return CommonReturnType.create(ret);
    }

}
