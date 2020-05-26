
package com.example.springwebserver.dao;

import com.example.springwebserver.dataObject.MallOrderItemDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallOrderItemMapper {
    int deleteByPrimaryKey(Long orderItemId);

    int insert(MallOrderItemDO record);

    int insertSelective(MallOrderItemDO record);

    MallOrderItemDO selectByPrimaryKey(Long orderItemId);

    /**
     * 根据订单id获取订单项列表
     *
     * @param orderId
     * @return
     */
    List<MallOrderItemDO> selectByOrderId(Long orderId);

    /**
     * 根据订单ids获取订单项列表
     *
     * @param orderIds
     * @return
     */
    List<MallOrderItemDO> selectByOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 批量insert订单项数据
     *
     * @param orderItems
     * @return
     */
    int insertBatch(@Param("orderItems") List<MallOrderItemDO> orderItems);

    int updateByPrimaryKeySelective(MallOrderItemDO record);

    int updateByPrimaryKey(MallOrderItemDO record);
}