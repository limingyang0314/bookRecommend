
package com.example.springwebserver.dataObject;

import lombok.Data;

import java.util.Date;
@Data
public class MallOrderItemDO {
    private Long orderItemId;

    private Long orderId;

    private Long goodsId;

    private String goodsName;

    private String coverUrl;

    private Integer sellingPrice;

    private Integer goodsCount;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", orderItemId=").append(orderItemId);
        sb.append(", orderId=").append(orderId);
        sb.append(", goodsId=").append(goodsId);
        sb.append(", goodsName=").append(goodsName);
        sb.append(", goodsCoverImg=").append(coverUrl);
        sb.append(", sellingPrice=").append(sellingPrice);
        sb.append(", goodsCount=").append(goodsCount);
        sb.append("]");
        return sb.toString();
    }
}