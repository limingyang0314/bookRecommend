package com.example.springwebserver.controller.viewObject;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("图书商品实体")
public class GoodsVO {
    private Long goodsId;

    private String goodsName;

    private String goodsIntro;

    private String goodsCoverImg;

    private Integer sellingPrice;
}
