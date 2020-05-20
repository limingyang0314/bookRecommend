package com.example.springwebserver.controller;


import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.response.CommonReturnType;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("recommend")
@RequestMapping("/recommend")
@Slf4j
@Api(tags = "推荐相关接口", value = "提供个性化推荐相关的 Rest API")
public class RecommendController {

    @GetMapping("/testToken")
    @ResponseBody
    public CommonReturnType testToken() throws BusinessException {

        return CommonReturnType.create(null);

    }
}
