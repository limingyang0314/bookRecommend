
package com.example.springwebserver.controller.viewObject;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel("购物车页面购物项实体")
public class MallShoppingCartItemVO implements Serializable {

    private Long cartItemId;

    private Long goodsId;

    private Integer goodsCount;

    private String goodsName;

    private String goodsCoverImg;

    private Integer sellingPrice;
    private Integer totalPrice;

}
