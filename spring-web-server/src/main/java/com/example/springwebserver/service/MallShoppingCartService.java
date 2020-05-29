package com.example.springwebserver.service;

import com.example.springwebserver.controller.viewObject.MallShoppingCartItemVO;
import com.example.springwebserver.dataObject.MallShoppingCartItemDO;
import com.example.springwebserver.exception.BusinessException;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface MallShoppingCartService {
    /**
     * 保存商品至购物车中
     *
     * @param MallShoppingCartItem
     * @return
     */
    MallShoppingCartItemVO saveMallCartItem(MallShoppingCartItemDO MallShoppingCartItem) ;

    /**
     * 修改购物车中的属性
     * @param mallShoppingCartItemId
     * @param goodsCount
     * @return
     */
    MallShoppingCartItemDO updateMallCartItem(Long mallShoppingCartItemId,int goodsCount) throws BusinessException;

    /**
     * 获取购物项详情
     *
     * @param mallShoppingCartItemId
     * @return
     */
    MallShoppingCartItemVO getMallCartItemById(Long mallShoppingCartItemId);

    /**
     * 删除购物车中的商品
     *
     * @param mallShoppingCartItemId
     * @return
     */
    Boolean deleteById(Long mallShoppingCartItemId);

    /**
     * 获取我的购物车中的列表数据
     *
     * @param MallUserId
     * @return
     */
    List<MallShoppingCartItemVO> getMyShoppingCartItems( Long MallUserId);
    List<MallShoppingCartItemVO> getMallCartItemById(List<Long> itemIds);
    List<MallShoppingCartItemVO> saveMallCartItems(List<MallShoppingCartItemDO> mallShoppingCartItems) ;
}
