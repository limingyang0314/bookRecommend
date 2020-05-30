
package com.example.springwebserver.dataObject;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
//订单类
@Data
public class MallOrderDO {
    private Long orderId;
    private Long userId;
    private Integer totalPrice;
    private String orderStatus;
    private String userAddress;
    private String orderNo;
    private String createTime;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", orderId=").append(orderId);
        sb.append(", orderNo=").append(orderNo);
        sb.append(", userId=").append(userId);
        sb.append(", totalPrice=").append(totalPrice);
        sb.append(", orderStatus=").append(orderStatus);
        sb.append(", userAddress=").append(userAddress);
        sb.append("]");
        return sb.toString();
    }
}