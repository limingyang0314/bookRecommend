
package com.example.springwebserver.dao;

import com.example.springwebserver.dataObject.MallOrderDO;
import com.example.springwebserver.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallOrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(MallOrderDO record);

    int insertSelective(MallOrderDO record);

    MallOrderDO selectByPrimaryKey(Long orderId);

    MallOrderDO selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(MallOrderDO record);

    int updateByPrimaryKey(MallOrderDO record);

    List<MallOrderDO> findMallOrderList(PageQueryUtil pageUtil);

    int getTotalMallOrders(PageQueryUtil pageUtil);

    List<MallOrderDO> selectByPrimaryKeys(@Param("orderIds") List<Long> orderIds);

    int checkOut(@Param("orderIds") List<Long> orderIds);

    int closeOrder(@Param("orderIds") List<Long> orderIds, @Param("orderStatus") int orderStatus);

    int checkDone(@Param("orderIds") List<Long> asList);

}