package com.example.springwebserver.controller;

import com.example.springwebserver.controller.viewObject.MallOrderDetailVO;
//import com.example.springwebserver.controller.viewObject.MallShoppingCartItemVO;
import com.example.springwebserver.controller.viewObject.MallShoppingCartItemVO;
import com.example.springwebserver.controller.viewObject.UserVO;
import com.example.springwebserver.dataObject.UserDO;
import com.example.springwebserver.enums.EmBusinessError;
//import com.example.springwebserver.enums.MallException;
import com.example.springwebserver.enums.ServiceResultEnum;
import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.response.CommonReturnType;
import com.example.springwebserver.service.MallOrderService;
import com.example.springwebserver.service.MallShoppingCartService;
import com.example.springwebserver.service.UserService;
import com.example.springwebserver.service.model.UserModel;
import com.example.springwebserver.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller("order")
@RequestMapping("/order")
@Slf4j
@Api(tags = "订单相关接口", value = "提供订单相关的 Rest API")
public class MallOrderController {
    @Autowired
    private MallShoppingCartService mallShoppingCartService;
    @Autowired
    private MallOrderService mallOrderService;
    @Autowired
    private UserService userService;


    @ApiOperation("根据订单编号获取订单详情")
    @GetMapping("/detail")
    @ResponseBody
    //HttpServletRequest request,
    public CommonReturnType orderDetailPage( @RequestParam(name = "orderNo") String orderNo) throws BusinessException {
        UserModel user = userService.getUserByToken();
        MallOrderDetailVO orderDetailVO = mallOrderService.getOrderDetailByOrderNo(orderNo);
        if (orderDetailVO == null) {
            log.warn("==== [get order] ==== order not exit");
            throw new BusinessException(EmBusinessError.BOOK_NOT_EXIST);
        }
//        request.setAttribute("orderDetailVO", orderDetailVO);
        return CommonReturnType.create(orderDetailVO);
    }

    @ApiOperation("获取我的订单")
    @GetMapping("/history")
    @ResponseBody
    public CommonReturnType orderListPage() throws BusinessException {
        UserModel user = userService.getUserByToken();
        Map<String, Object> params = new HashMap<>();
        params.put("userId",user.getUserId());
        if (StringUtils.isEmpty(params.get("page"))) {
            params.put("page", 1);
        }
        params.put("limit", 20);
        //封装我的订单数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        PageResult pageResult = mallOrderService.getMyOrders(pageUtil);
        if(pageResult==null){
            log.warn("==== [get myorder] ==== myorder not exit");
            throw new BusinessException(EmBusinessError.MYORDER_NOT_EXIST);
        }
        return CommonReturnType.create(pageResult);
    }

    @ApiOperation("取消订单")
    @GetMapping("/cancel")
    @ResponseBody
    public Result cancelOrder(@RequestParam (name = "orderNo") String orderNo) throws BusinessException {
        UserModel user = userService.getUserByToken();
        String cancelOrderResult = mallOrderService.cancelOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(cancelOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(cancelOrderResult);
        }
    }

    @ApiOperation("完成订单")
    @GetMapping("/finish")
    @ResponseBody
    public Result finishOrder(@RequestParam (name = "orderNo") String orderNo) throws BusinessException {
        UserModel user = userService.getUserByToken();
        String finishOrderResult = mallOrderService.finishOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(finishOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(finishOrderResult);
        }
    }

    @ApiOperation("下单")
    @PostMapping("/create")
    @ResponseBody
    public String saveOrder(@RequestParam(value = "itemIds[]")Long[] itemIds) throws BusinessException {
        List<Long> orderIds = new ArrayList<>();
        Collections.addAll(orderIds,itemIds);
        UserModel user = userService.getUserByToken();
        List<MallShoppingCartItemVO> myShoppingCartItems = mallShoppingCartService.getMallCartItemById(orderIds);
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            //购物车中无数据
            throw new BusinessException(EmBusinessError.SHOPPING_ITEM_ERROR);
        }
        //保存订单并返回订单号
        String saveOrderResult = mallOrderService.saveOrder(user, myShoppingCartItems);
        //跳转到订单详情页
        return saveOrderResult;
    }

}
