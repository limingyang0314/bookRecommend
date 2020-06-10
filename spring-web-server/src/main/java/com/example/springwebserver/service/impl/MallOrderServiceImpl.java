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

import java.awt.print.Book;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class MallOrderServiceImpl implements MallOrderService {

    @Autowired
    private MallOrderMapper mallOrderMapper;
    @Autowired
    private MallOrderItemMapper mallOrderItemMapper;
    @Autowired
    private MallShoppingCartItemMapper mallShoppingCartItemMapper;
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
            if (mallShoppingCartItemMapper.deleteBatch(itemIdList) > 0) {
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
                mallOrder.setOrderStatus("已下单");
                Date currentTime = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(currentTime);
                mallOrder.setCreateTime(dateString);

                //todo 订单body字段，用来作为生成支付单描述信息，暂时未接入第三方支付接口，故该字段暂时设为空字符串

                //生成订单项并保存订单项纪录
                if (mallOrderMapper.insertSelective(mallOrder) > 0) {
                    //生成所有的订单项快照，并保存至数据库
                    List<MallOrderItemDO> mallOrderItems = new ArrayList<>();
                    for (MallShoppingCartItemVO MallShoppingCartItemVO : myShoppingCartItems) {
                        MallOrderItemDO mallOrderItem = new MallOrderItemDO();
                        //使用BeanUtil工具类将MallShoppingCartItemVO中的属性复制到MallOrderItem对象中
                        BeanUtil.copyProperties(MallShoppingCartItemVO, mallOrderItem);
                        //MallOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
                        mallOrderItem.setOrderId(mallOrder.getOrderId());
                        mallOrderItem.setCreateTime(mallOrder.getCreateTime());
                        mallOrderItems.add(mallOrderItem);
                    }
                    //保存至数据库
                    if (mallOrderItemMapper.insertBatch(mallOrderItems) > 0) {
                        //所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
                        return orderNo;
                    }else
                        throw new BusinessException(EmBusinessError.DB_ERROR);

                }
            }
        }else
            throw new BusinessException(EmBusinessError.SHOPPING_ITEM_ERROR);
        return "create order successfully!";
    }

    @Override
    public MallOrderDetailVO getOrderDetailByOrderNo(String orderNo) {
        //通过订单编号返回订单实体
        MallOrderDO MallOrder = mallOrderMapper.selectByOrderNo(orderNo);
        if (MallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //通过订单编号，查询该订单编号下所有的商品实体
            List<MallOrderItemDO> orderItems = mallOrderItemMapper.selectByOrderId(MallOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                //把商品实体copy到vo类
                List<MallOrderItemVO> MallOrderItemVOS = BeanUtil.copyList(orderItems, MallOrderItemVO.class);
                //创建订单详情vo
                MallOrderDetailVO MallOrderDetailVO = new MallOrderDetailVO();
                //把order实体中的属性copy给订单详情vo实体
                BeanUtil.copyProperties(MallOrder, MallOrderDetailVO);
                MallOrderDetailVO.setOrderStatus(MallOrderDetailVO.getOrderStatus()+"");
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
    public PageResult getAllOrders(PageQueryUtil pageUtil) throws BusinessException {
        int total = mallOrderMapper.getTotalMallOrders(pageUtil);
        List<MallOrderDO> orderDOS = mallOrderMapper.findMallOrderList(pageUtil);
        if(total==0)
            throw new BusinessException(EmBusinessError.ORDER_NOT_EXIST);
        //可能有多个订单
        List<MallOrderDetailVO> detailVOS = new ArrayList<>();
        detailVOS = BeanUtil.copyList(orderDOS,MallOrderDetailVO.class);

        List<Long> orderIds = orderDOS.stream().map(MallOrderDO::getOrderId).collect(Collectors.toList());
        if(!orderDOS.isEmpty()){
            List<MallOrderItemDO> itemDOS = mallOrderItemMapper.selectByOrderIds(orderIds);
            List<Long> bookIds = itemDOS.stream().map(MallOrderItemDO::getGoodsId).collect(Collectors.toList());
            List<BookDO> bookDOS = bookDOMapper.selectByPrimaryKeys(bookIds);
            System.out.println(bookDOS.get(0).toString());
            System.out.println(itemDOS.get(1).toString());

            Map<Long,MallOrderItemDO> itemByBookIdMap = itemDOS.stream().collect(Collectors.toMap(MallOrderItemDO::getGoodsId,mallOrderItemDO->mallOrderItemDO,(e1,e2)->e1));
            Map<Long,BookDO> bookByBookIdMap = bookDOS.stream().collect(Collectors.toMap(BookDO::getBookId,bookDO -> bookDO,(e1,e2)->e1));

            for(Long book_id:bookByBookIdMap.keySet()){
                if(itemByBookIdMap.containsKey(book_id)){
                    MallOrderItemDO mallOrderItemDO = itemByBookIdMap.get(book_id);
                    BookDO bookDO = bookByBookIdMap.get(book_id);
                    String url = bookDO.getCoverUrl();
                    mallOrderItemDO.setGoodsCoverImg(url);
                    itemByBookIdMap.put(book_id,mallOrderItemDO);
                }
            }
            itemDOS = new ArrayList<>(itemByBookIdMap.values());
            Map<Long,List<MallOrderItemDO>> itemByOrderIdMap = itemDOS.stream().collect(groupingBy(MallOrderItemDO::getOrderId));

            for(MallOrderDetailVO mallOrderDetailVO:detailVOS){
                if(itemByOrderIdMap.containsKey(mallOrderDetailVO.getOrderId())){
                    List<MallOrderItemDO> mallOrderItemDOS = itemByOrderIdMap.get(mallOrderDetailVO.getOrderId());
                    List<MallOrderItemVO> mallOrderItemVOS = BeanUtil.copyList(mallOrderItemDOS,MallOrderItemVO.class);
                    System.out.println(mallOrderDetailVO);
                    mallOrderDetailVO.setMallOrderItemVOS(mallOrderItemVOS);
                }
            }
        }
        PageResult pageResult = new PageResult(detailVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) throws BusinessException {
        int total = mallOrderMapper.getTotalMallOrders(pageUtil);
        List<MallOrderDO> orderDOS = mallOrderMapper.findMallOrderList(pageUtil);
        if(total==0)
            throw new BusinessException(EmBusinessError.ORDER_NOT_EXIST);
        //可能有多个订单
        List<MallOrderDetailVO> detailVOS = new ArrayList<>();
        detailVOS = BeanUtil.copyList(orderDOS,MallOrderDetailVO.class);

        List<Long> orderIds = orderDOS.stream().map(MallOrderDO::getOrderId).collect(Collectors.toList());
        if(!orderDOS.isEmpty()){
            List<MallOrderItemDO> itemDOS = mallOrderItemMapper.selectByOrderIds(orderIds);
            List<Long> bookIds = itemDOS.stream().map(MallOrderItemDO::getGoodsId).collect(Collectors.toList());
            List<BookDO> bookDOS = bookDOMapper.selectByPrimaryKeys(bookIds);
            System.out.println(bookDOS.get(0).toString());
            System.out.println(itemDOS.get(1).toString());

            Map<Long,MallOrderItemDO> itemByBookIdMap = itemDOS.stream().collect(Collectors.toMap(MallOrderItemDO::getGoodsId,mallOrderItemDO->mallOrderItemDO,(e1,e2)->e1));
            Map<Long,BookDO> bookByBookIdMap = bookDOS.stream().collect(Collectors.toMap(BookDO::getBookId,bookDO -> bookDO,(e1,e2)->e1));

            for(Long book_id:bookByBookIdMap.keySet()){
                if(itemByBookIdMap.containsKey(book_id)){
                    MallOrderItemDO mallOrderItemDO = itemByBookIdMap.get(book_id);
                    BookDO bookDO = bookByBookIdMap.get(book_id);
                    String url = bookDO.getCoverUrl();
                    mallOrderItemDO.setGoodsCoverImg(url);
                    itemByBookIdMap.put(book_id,mallOrderItemDO);
                }
            }
            itemDOS = new ArrayList<>(itemByBookIdMap.values());
            Map<Long,List<MallOrderItemDO>> itemByOrderIdMap = itemDOS.stream().collect(groupingBy(MallOrderItemDO::getOrderId));

            for(MallOrderDetailVO mallOrderDetailVO:detailVOS){
                if(itemByOrderIdMap.containsKey(mallOrderDetailVO.getOrderId())){
                    List<MallOrderItemDO> mallOrderItemDOS = itemByOrderIdMap.get(mallOrderDetailVO.getOrderId());
                    List<MallOrderItemVO> mallOrderItemVOS = BeanUtil.copyList(mallOrderItemDOS,MallOrderItemVO.class);
                    System.out.println(mallOrderDetailVO);
                    mallOrderDetailVO.setMallOrderItemVOS(mallOrderItemVOS);
                }
            }
        }
        PageResult pageResult = new PageResult(detailVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;

    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        MallOrderDO mallOrder = mallOrderMapper.selectByOrderNo(orderNo);
        mallOrder.setOrderStatus("手动取消");
        if (mallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            if (mallOrderMapper.closeOrder(mallOrder.getOrderId(), mallOrder.getOrderStatus()) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        MallOrderDO mallOrder = mallOrderMapper.selectByOrderNo(orderNo);
        if (mallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            mallOrder.setOrderStatus("订单完成");
            if (mallOrderMapper.updateByPrimaryKeySelective(mallOrder) > 0) {
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
