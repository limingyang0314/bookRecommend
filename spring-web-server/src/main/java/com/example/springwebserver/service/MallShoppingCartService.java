package com.example.springwebserver.service;

import com.example.springwebserver.controller.viewObject.MallShoppingCartItemVO;
import com.example.springwebserver.dataObject.MallShoppingCartItemDO;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface MallShoppingCartService {
    /**
     * 保存商品至购物车中
     *
     * @param MallShoppingCartItem
     * @return
     */
    String saveMallCartItem(MallShoppingCartItemDO MallShoppingCartItem);

    /**
     * 修改购物车中的属性
     *
     * @param MallShoppingCartItem
     * @return
     */
    String updateMallCartItem(MallShoppingCartItemDO MallShoppingCartItem);

    /**
     * 获取购物项详情
     *
     * @param MallShoppingCartItemId
     * @return
     */
    MallShoppingCartItemDO getMallCartItemById(Long MallShoppingCartItemId);

    /**
     * 删除购物车中的商品
     *
     * @param MallShoppingCartItemId
     * @return
     */
    Boolean deleteById(Long MallShoppingCartItemId);

    /**
     * 获取我的购物车中的列表数据
     *
     * @param MallUserId
     * @return
     */
    List<MallShoppingCartItemVO> getMyShoppingCartItems( Long MallUserId);
}
