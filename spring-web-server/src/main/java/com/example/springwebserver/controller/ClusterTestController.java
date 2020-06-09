package com.example.springwebserver.controller;

import com.example.springwebserver.response.CommonReturnType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * 本控制器用于对服务端各种集群进行测试
 */

@Controller("clusterTest")
@RequestMapping("/clusterTest")
@Slf4j
@Api(tags = "集群测试接口", value = "测试集群使用的 Rest API")
public class ClusterTestController {

    @ApiOperation("返回当前后端节点的ID")
    @GetMapping("/backEnd")
    @ResponseBody
    public CommonReturnType backEnd(){
        HashMap<String,String> ret = new HashMap<>();
        int clusterID = 1;
        ret.put("back-end ID", Integer.toString(clusterID));
        return CommonReturnType.create(ret);
    }

}
