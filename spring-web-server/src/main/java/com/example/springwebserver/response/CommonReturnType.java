package com.example.springwebserver.response;

import lombok.Data;

@Data
public class CommonReturnType {
    private boolean status;

    private Object data;

    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,true);
    }

    public static CommonReturnType create(Object result, boolean status){
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }
}
