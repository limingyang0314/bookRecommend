package com.example.springwebserver.controller;

import com.example.springwebserver.controller.viewObject.MallOrderDetailVO;
import com.example.springwebserver.controller.viewObject.MallShoppingCartItemVO;
import com.example.springwebserver.controller.viewObject.UserVO;
import com.example.springwebserver.enums.EmBusinessError;
import com.example.springwebserver.enums.MallException;
import com.example.springwebserver.enums.ServiceResultEnum;
import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.response.CommonReturnType;
import com.example.springwebserver.service.MallOrderService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("order")
@RequestMapping("/order")
@Slf4j
@Api(tags = "订单相关接口", value = "提供订单相关的 Rest API")
public class MallOrderController {
//    @Resource
//    private MallShoppingCartService MallShoppingCartService;
    @Resource
    private MallOrderService MallOrderService;

    @ApiOperation("根据订单编号获取订单详情")
    @GetMapping("/orderdetail")
    @ResponseBody
    //HttpServletRequest request,
    public CommonReturnType orderDetailPage( @RequestParam(name = "orderNo") String orderNo) throws BusinessException {
//        UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        MallOrderDetailVO orderDetailVO = MallOrderService.getOrderDetailByOrderNo(orderNo);
        if (orderDetailVO == null) {
            log.warn("==== [get order] ==== order not exit");
            throw new BusinessException(EmBusinessError.BOOK_NOT_EXIST);
        }
//        request.setAttribute("orderDetailVO", orderDetailVO);
        return CommonReturnType.create(orderDetailVO);
    }

    @ApiOperation("获取我的订单")
    @GetMapping("/myorder")
    @ResponseBody
    public CommonReturnType orderListPage(@RequestParam Map<String, Object> params) throws BusinessException {
        //todo 加入session用户验证
//        UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        ;
//        params.put("userId", 1);
        if (StringUtils.isEmpty(params.get("page"))) {
            params.put("page", 1);
        }
        params.put("limit", Constants.ORDER_SEARCH_PAGE_LIMIT);
        //封装我的订单数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        PageResult pageResult = MallOrderService.getMyOrders(pageUtil);
        if(pageResult==null){
            log.warn("==== [get myorder] ==== myorder not exit");
            throw new BusinessException(EmBusinessError.MYORDER_NOT_EXIST);
        }
        return CommonReturnType.create(pageResult);
    }
//    @GetMapping("/createOrder")
//    public String saveOrder(@RequestParam (name = "userid") String userid) {
////        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
//        List<MallShoppingCartItemVO> myShoppingCartItems = MallShoppingCartService.getMyShoppingCartItems(userid);
//        //todo 处理收获地址
////        if (StringUtils.isEmpty(user.getAddress().trim())) {
////            //无收货地址
////            MallException.fail(ServiceResultEnum.NULL_ADDRESS_ERROR.getResult());
////        }
//        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
//            //购物车中无数据则跳转至错误页
//            MallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
//        }
//        //保存订单并返回订单号
//        String saveOrderResult = MallOrderService.saveOrder(user, myShoppingCartItems);
//        //跳转到订单详情页
//        return "redirect:/orders/" + saveOrderResult;
//    }
    @ApiOperation("取消订单")
    @GetMapping("/orders/cancel")
    @ResponseBody
    public Result cancelOrder(@RequestParam (name = "userId") Long userId,@RequestParam (name = "orderNo") String orderNo) {
//        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        String cancelOrderResult = MallOrderService.cancelOrder(orderNo, userId);
        if (ServiceResultEnum.SUCCESS.getResult().equals(cancelOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(cancelOrderResult);
        }
    }

    @ApiOperation("完成订单")
    @PutMapping("/orders/{orderNo}/finish")
    @ResponseBody
    public Result finishOrder(@RequestParam (name = "userId") Long userId,@RequestParam (name = "orderNo") String orderNo) {
//        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        String finishOrderResult = MallOrderService.finishOrder(orderNo, userId);
        if (ServiceResultEnum.SUCCESS.getResult().equals(finishOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(finishOrderResult);
        }
    }

}
