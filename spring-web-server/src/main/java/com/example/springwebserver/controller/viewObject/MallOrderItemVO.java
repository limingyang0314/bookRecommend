
package com.example.springwebserver.controller.viewObject;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("订单详情-单个订单实体")
public class MallOrderItemVO implements Serializable {

    private Long goodsId;

    private Integer goodsCount;

    private String goodsName;

    private String goodsCoverImg;

    private Integer sellingPrice;
    private String createTime;

}
