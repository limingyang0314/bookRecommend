/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.example.springwebserver.dao;

import com.example.springwebserver.dataObject.MallGoods;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallGoodsMapper {
    int deleteByPrimaryKey(Long goodsId);

    int insert(MallGoods record);

    int insertSelective(MallGoods record);

    MallGoods selectByPrimaryKey(Long goodsId);

    int updateByPrimaryKeySelective(MallGoods record);

    int updateByPrimaryKeyWithBLOBs(MallGoods record);

    int updateByPrimaryKey(MallGoods record);

//    List<MallGoods> findMallGoodsList(PageQueryUtil pageUtil);
//
//    int getTotalMallGoods(PageQueryUtil pageUtil);
//
//    List<MallGoods> selectByPrimaryKeys(List<Long> goodsIds);
//
//    List<MallGoods> findMallGoodsListBySearch(PageQueryUtil pageUtil);
//
//    int getTotalMallGoodsBySearch(PageQueryUtil pageUtil);
//
//    int batchInsert(@Param("MallGoodsList") List<MallGoods> MallGoodsList);
//
//    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int batchUpdateSellStatus(@Param("orderIds") Long[] orderIds, @Param("sellStatus") int sellStatus);

}