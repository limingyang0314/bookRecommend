/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.example.springwebserver.service.impl;

import com.example.springwebserver.controller.viewObject.MallShoppingCartItemVO;
import com.example.springwebserver.dao.*;
import com.example.springwebserver.dataObject.BookDO;
import com.example.springwebserver.dataObject.MallShoppingCartItemDO;
import com.example.springwebserver.service.MallShoppingCartService;
import com.example.springwebserver.util.BeanUtil;
import com.example.springwebserver.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MallShoppingCartServiceImpl implements MallShoppingCartService {

    @Autowired
    private MallShoppingCartItemMapper mallShoppingCartItemMapper;

    @Autowired
    private MallOrderMapper mallOrderMapper;
    @Autowired
    private MallOrderItemMapper mallOrderItemMapper;
    @Autowired
    private BookDOMapper bookDOMapper;

    @Override
    public String saveMallCartItem(MallShoppingCartItemDO MallShoppingCartItem) {
        return null;
    }

    @Override
    public String updateMallCartItem(MallShoppingCartItemDO MallShoppingCartItem) {
        return null;
    }

    @Override
    public MallShoppingCartItemDO getMallCartItemById(Long MallShoppingCartItemId) {
        return null;
    }

    @Override
    public Boolean deleteById(Long MallShoppingCartItemId) {
        return null;
    }

    @Override
    public List<MallShoppingCartItemVO> getMyShoppingCartItems(Long MallUserId) {
        List<MallShoppingCartItemVO> mallShoppingCartItemVOS = new ArrayList<>();
        List<MallShoppingCartItemDO> mallShoppingCartItemDOS = mallShoppingCartItemMapper.selectByUserId(MallUserId);
        if (!CollectionUtils.isEmpty(mallShoppingCartItemDOS)) {
            //查询商品信息并做数据转换
            List<Long> mallGoodsIds = mallShoppingCartItemDOS.stream().map(MallShoppingCartItemDO::getGoodsId).collect(Collectors.toList());
            List<BookDO> mallGoodsDO = bookDOMapper.selectByPrimaryKeys(mallGoodsIds);
            Map<Long, BookDO> mallGoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(mallGoodsDO)) {
                mallGoodsMap = mallGoodsDO.stream().collect(Collectors.toMap(BookDO::getBookId, Function.identity(), (entity1, entity2) -> entity1));
            }
            for (MallShoppingCartItemDO mallShoppingCartItem : mallShoppingCartItemDOS) {
                MallShoppingCartItemVO mallShoppingCartItemVO = new MallShoppingCartItemVO();
                BeanUtil.copyProperties(mallShoppingCartItem, mallShoppingCartItemVO);
                if (mallGoodsMap.containsKey(mallShoppingCartItem.getGoodsId())) {
                    BookDO mallGoodsDOTemp = mallGoodsMap.get(mallShoppingCartItem.getGoodsId());
//                    MallShoppingCartItemVO.setGoodscoverImg(mallGoodsDOTemp.getGoodsCoverImg());
                    String goodsName = mallGoodsDOTemp.getBookName();
                    // 字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    mallShoppingCartItemVO.setGoodsName(goodsName);
                    mallShoppingCartItemVO.setSellingPrice(mallGoodsDOTemp.getPrice());
                    mallShoppingCartItemVOS.add(mallShoppingCartItemVO);
                }
            }
        }
        return mallShoppingCartItemVOS;
    }

    //todo 修改session中购物项数量

    
}
