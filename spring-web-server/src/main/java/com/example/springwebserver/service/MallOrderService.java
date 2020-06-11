package com.example.springwebserver.service;

//import com.example.demodeal.domain.Goods;
import com.example.springwebserver.controller.viewObject.MallOrderDetailVO;
import com.example.springwebserver.controller.viewObject.MallOrderItemVO;
//import com.example.springwebserver.controller.viewObject.MallShoppingCartItemVO;
import com.example.springwebserver.controller.viewObject.MallShoppingCartItemVO;
import com.example.springwebserver.controller.viewObject.UserVO;
import com.example.springwebserver.dataObject.MallOrderDO;
import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.service.model.UserModel;
import com.example.springwebserver.util.PageQueryUtil;
import com.example.springwebserver.util.PageResult;
import lombok.Data;

import java.util.List;

//import com.example.springwebserver.service.model.OrderModel;
public interface MallOrderService {


    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
//    PageResult getMallOrderDOsPage(PageQueryUtil pageUtil);

    /**
     * 订单信息修改
     *
     * @param MallOrderDO
     * @return
     */
    String updateOrderInfo(MallOrderDO MallOrderDO);

    /**
     * 配货
     *
     * @param ids
     * @return
     */
    String checkDone(Long[] ids);

    /**
     * 出库
     *
     * @param ids
     * @return
     */
    String checkOut(Long[] ids);

    /**
     * 关闭订单
     *
     * @param ids
     * @return
     */
    String closeOrder(Long[] ids);

    /**
     * 保存订单
     *
     * @param user
     * @param myShoppingCartItems
     * @return
     */
    String saveOrder(UserModel user, List<MallShoppingCartItemVO> myShoppingCartItems) throws BusinessException;

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @param
     * @return
     */
    MallOrderDetailVO getOrderDetailByOrderNo(String orderNo);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @return
     */
    MallOrderDO getMallOrderDOByOrderNo(String orderNo);

    /**
     * 我的订单列表
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyOrders(PageQueryUtil pageUtil) throws BusinessException;

    /**
     * 所有订单列表
     *
     * @param pageUtil
     * @return
     */
    PageResult getAllOrders(PageQueryUtil pageUtil) throws BusinessException;

    /**
     * 手动取消订单
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String cancelOrder(String orderNo, Long userId);

    /**
     * 确认收货
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String finishOrder(String orderNo, Long userId);

    String paySuccess(String orderNo, int payType);

    List<MallOrderItemVO> getOrderItems(Long id);

    int getOrderCount();

}

