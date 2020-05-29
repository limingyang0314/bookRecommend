
package com.example.springwebserver.controller.viewObject;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("订单详情-页面实体")
public class MallOrderDetailVO implements Serializable {

    private Long orderId;
    private String orderNo;
    private Integer totalPrice;

    private String orderStatus;

    private String userAddress;

    private List<MallOrderItemVO> mallOrderItemVOS;



}
