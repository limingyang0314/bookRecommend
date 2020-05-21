package com.example.springwebserver.controller.viewObject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



@Data
@ApiModel("用户实体")
public class UserVO {

    private Long userId;

    private String userName;

    private Integer age;

    @ApiModelProperty("false 为 男， true 为 女")
    private Boolean gender;

    private String introduction;

}
