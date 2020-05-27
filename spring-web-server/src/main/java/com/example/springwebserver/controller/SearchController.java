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

@Controller("search")
@RequestMapping("/search")
@Slf4j
@Api(tags = "搜索相关接口", value = "提供搜索相关的 Rest API")
public class SearchController extends GlobalExceptionHandler {

}
