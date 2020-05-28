
package com.example.springwebserver.dao;

import com.example.springwebserver.dataObject.MallShoppingCartItemDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallShoppingCartItemMapper {
    int deleteByPrimaryKey(Long cartItemId);

    int insert(MallShoppingCartItemDO record);

    int insertSelective(MallShoppingCartItemDO record);

    MallShoppingCartItemDO selectByPrimaryKey(Long cartItemId);

    MallShoppingCartItemDO selectByUserIdAndGoodsId(@Param("MallUserId") Long UserId, @Param("goodsId") Long goodsId);

    List<MallShoppingCartItemDO> selectByUserId(@Param("UserId") Long UserId);

    int selectCountByUserId(Long UserId);

    int updateByPrimaryKeySelective(MallShoppingCartItemDO record);

    int updateByPrimaryKey(MallShoppingCartItemDO record);

    int deleteBatch(List<Long> ids);
}