package com.example.springwebserver.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("admin")
@RequestMapping("/admin")
@Slf4j
@Api(tags = "管理后台接口", value = "提供基础后台管理的 Rest API")
public class AdminController {

}
