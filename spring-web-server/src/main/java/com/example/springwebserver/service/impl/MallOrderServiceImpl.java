package com.example.springwebserver.service.impl;

import com.example.springwebserver.controller.viewObject.*;
import com.example.springwebserver.dao.*;
import com.example.springwebserver.dataObject.BookDO;
import com.example.springwebserver.dataObject.MallOrderDO;
import com.example.springwebserver.dataObject.MallOrderItemDO;
import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.service.MallOrderService;
import com.example.springwebserver.service.model.UserModel;
import com.example.springwebserver.util.BeanUtil;
import com.example.springwebserver.util.NumberUtil;
import com.example.springwebserver.util.PageQueryUtil;
import com.example.springwebserver.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.springwebserver.dao.MallOrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.example.springwebserver.enums.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class MallOrderServiceImpl implements MallOrderService {

    @Autowired
    private MallOrderMapper MallOrderMapper;
    @Autowired
    private MallOrderItemMapper MallOrderItemMapper;
    @Autowired
    private MallShoppingCartItemMapper MallShoppingCartItemMapper;
    @Autowired
    private BookDOMapper bookDOMapper;

    @Override
    public String updateOrderInfo(MallOrderDO MallOrderDO) {
        return null;
    }

    @Override
    public String checkDone(Long[] ids) {
        return null;
    }

    @Override
    public String checkOut(Long[] ids) {
        return null;
    }

    @Override
    public String closeOrder(Long[] ids) {
        return null;
    }

    @Override
    public String saveOrder(UserModel user, List<MallShoppingCartItemVO> myShoppingCartItems) throws BusinessException {
        List<Long> itemIdList = myShoppingCartItems.stream().map(MallShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = myShoppingCartItems.stream().map(MallShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
        List<BookDO> bookDOS = bookDOMapper.selectByPrimaryKeys(goodsIds);
        Map<Long, BookDO> BooksMap = bookDOS.stream().collect(Collectors.toMap(BookDO::getBookId, Function.identity(), (entity1, entity2) -> entity1));
        //删除购物项
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds)) {
            if (MallShoppingCartItemMapper.deleteBatch(itemIdList) > 0) {
                //生成订单号
                String orderNo = NumberUtil.getOrderNo();
                int priceTotal = 0;
                //保存订单
                MallOrderDO mallOrder = new MallOrderDO();
                mallOrder.setOrderNo(orderNo);
                mallOrder.setUserId(user.getUserId());
                mallOrder.setUserAddress(user.getAddress());
                //总价
                for (MallShoppingCartItemVO MallShoppingCartItemVO : myShoppingCartItems) {
                    priceTotal += MallShoppingCartItemVO.getGoodsCount() * MallShoppingCartItemVO.getSellingPrice();
                }
                mallOrder.setTotalPrice(priceTotal);
                //todo 订单body字段，用来作为生成支付单描述信息，暂时未接入第三方支付接口，故该字段暂时设为空字符串

                //生成订单项并保存订单项纪录
                if (MallOrderMapper.insertSelective(mallOrder) > 0) {
                    //生成所有的订单项快照，并保存至数据库
                    List<MallOrderItemDO> MallOrderItems = new ArrayList<>();
                    for (MallShoppingCartItemVO MallShoppingCartItemVO : myShoppingCartItems) {
                        MallOrderItemDO mallOrderItem = new MallOrderItemDO();
                        //使用BeanUtil工具类将MallShoppingCartItemVO中的属性复制到MallOrderItem对象中
                        BeanUtil.copyProperties(MallShoppingCartItemVO, mallOrderItem);
                        //MallOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
                        mallOrderItem.setOrderId(mallOrder.getOrderId());
                        MallOrderItems.add(mallOrderItem);
                    }
                    //保存至数据库
                    if (MallOrderItemMapper.insertBatch(MallOrderItems) > 0) {
                        //所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
                        return orderNo;
                    }

                }
            }
        }else
            throw new BusinessException(EmBusinessError.SHOPPING_ITEM_ERROR);
        return "create order successfully!";
    }

    @Override
    public MallOrderDetailVO getOrderDetailByOrderNo(String orderNo) {
        //通过订单编号返回订单实体
        MallOrderDO MallOrder = MallOrderMapper.selectByOrderNo(orderNo);
        if (MallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //通过订单编号，查询该订单编号下所有的商品实体
            List<MallOrderItemDO> orderItems = MallOrderItemMapper.selectByOrderId(MallOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                //把商品实体copy到vo类
                List<MallOrderItemVO> MallOrderItemVOS = BeanUtil.copyList(orderItems, MallOrderItemVO.class);
                //创建订单详情vo
                MallOrderDetailVO MallOrderDetailVO = new MallOrderDetailVO();
                //把order实体中的属性copy给订单详情vo实体
                BeanUtil.copyProperties(MallOrder, MallOrderDetailVO);
                MallOrderDetailVO.setOrderStatusString(MallOrderStatusEnum.getMallOrderStatusEnumByStatus(MallOrderDetailVO.getOrderStatus()).getName());
//                MallOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(MallOrderDetailVO.getPayType()).getName());
                MallOrderDetailVO.setMallOrderItemVOS(MallOrderItemVOS);
                return MallOrderDetailVO;
            }
        }
        return null;
    }

    @Override
    public MallOrderDO getMallOrderDOByOrderNo(String orderNo) {
        return null;
    }

    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) {
        //pageUtil是一个hashmap
        //得到所有订单数
        int total = MallOrderMapper.getTotalMallOrders(pageUtil);
        //得到所有订单
        List<MallOrderDO> MallOrders = MallOrderMapper.findMallOrderList(pageUtil);
        //创建返还给前端的VO类
        List<MallOrderListVO> orderListVOS = new ArrayList<>();
        if (total > 0) {
            //数据转换 将实体类转成vo
            orderListVOS = BeanUtil.copyList(MallOrders, MallOrderListVO.class);
            //设置订单状态
            for (MallOrderListVO MallOrderListVO : orderListVOS) {
                MallOrderListVO.setOrderStatusString(MallOrderStatusEnum.getMallOrderStatusEnumByStatus(MallOrderListVO.getOrderStatus()).getName());
            }
            //获取所有的订单编号
            List<Long> orderIds = MallOrders.stream().map(MallOrderDO::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                //根据订单编号获取所有订单里购买的商品
                List<MallOrderItemDO> orderItems = MallOrderItemMapper.selectByOrderIds(orderIds);
                //将订单号和订单商品映射
                Map<Long, List<MallOrderItemDO>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(MallOrderItemDO::getOrderId));
                //VO类是返回给前端的数据
                for (MallOrderListVO MallOrderListVO : orderListVOS) {
                    //封装每个订单列表对象的订单项数据
                    if (itemByOrderIdMap.containsKey(MallOrderListVO.getOrderId())) {
                        //获取每个订单对应的商品DO
                        List<MallOrderItemDO> orderItemListTemp = itemByOrderIdMap.get(MallOrderListVO.getOrderId());
                        //将MallOrderItem对象列表转换成MallOrderItemVO对象列表
                        List<MallOrderItemVO> MallOrderItemVOS = BeanUtil.copyList(orderItemListTemp, MallOrderItemVO.class);
                        //设置单个订单的所有商品VO
                        MallOrderListVO.setMallOrderItemVOS(MallOrderItemVOS);
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(orderListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        MallOrderDO MallOrder = MallOrderMapper.selectByOrderNo(orderNo);
        if (MallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            if (MallOrderMapper.closeOrder(Collections.singletonList(MallOrder.getOrderId()), MallOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        MallOrderDO MallOrder = MallOrderMapper.selectByOrderNo(orderNo);
        if (MallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            MallOrder.setOrderStatus((byte) MallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
//            MallOrder.setUpdateTime(new Date());
            if (MallOrderMapper.updateByPrimaryKeySelective(MallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String paySuccess(String orderNo, int payType) {
        return null;
    }

    @Override
    public List<MallOrderItemVO> getOrderItems(Long id) {
        return null;
    }
}
