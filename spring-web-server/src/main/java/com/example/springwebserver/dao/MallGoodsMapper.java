
package com.example.springwebserver.dao;

import com.example.springwebserver.dataObject.BookDO;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallGoodsMapper {
    int deleteByPrimaryKey(Long goodsId);

    int insert(BookDO record);

    int insertSelective(BookDO record);

    BookDO selectByPrimaryKey(Long goodsId);

    int updateByPrimaryKeySelective(BookDO record);

    int updateByPrimaryKeyWithBLOBs(BookDO record);

    int updateByPrimaryKey(BookDO record);

//    List<MallGoods> findMallGoodsList(PageQueryUtil pageUtil);

//    int getTotalMallGoods(PageQueryUtil pageUtil);

    List<BookDO> selectByPrimaryKeys(List<Long> goodsIds);

//    List<MallGoods> findMallGoodsListBySearch(PageQueryUtil pageUtil);

//    int getTotalMallGoodsBySearch(PageQueryUtil pageUtil);

    int batchInsert(@Param("MallGoodsList") List<BookDO> mallGoodsDOList);


    int batchUpdateSellStatus(@Param("orderIds") Long[] orderIds, @Param("sellStatus") int sellStatus);

}