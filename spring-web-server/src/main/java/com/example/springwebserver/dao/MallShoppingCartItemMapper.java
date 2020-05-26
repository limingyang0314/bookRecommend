
package com.example.springwebserver.dao;

import com.example.springwebserver.dataObject.MallShoppingCartItemDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallShoppingCartItemMapper {
    int deleteByPrimaryKey(Long cartItemId);

    int insert(MallShoppingCartItemDO record);

    int insertSelective(MallShoppingCartItemDO record);

    MallShoppingCartItemDO selectByPrimaryKey(Long cartItemId);

    MallShoppingCartItemDO selectByUserIdAndGoodsId(@Param("newBeeMallUserId") Long newBeeMallUserId, @Param("goodsId") Long goodsId);

    List<MallShoppingCartItemDO> selectByUserId(@Param("newBeeMallUserId") Long newBeeMallUserId, @Param("number") int number);

    int selectCountByUserId(Long newBeeMallUserId);

    int updateByPrimaryKeySelective(MallShoppingCartItemDO record);

    int updateByPrimaryKey(MallShoppingCartItemDO record);

    int deleteBatch(List<Long> ids);
}