package com.example.springwebserver.enums;


import com.example.springwebserver.exception.CommonError;

public enum EmBusinessError implements CommonError {

    PARAMETER_VALIDATION_ERROR(101, "illegal parameter"),
    UNKNOWN_ERROR(102, "unknown error"),

    // 20 start with, about user
    USER_NOT_EXIST(201, "user not exist"),
    USER_LOGIN_FAIL(202, "unmatched username and password"),
    USER_NOT_LOGIN(203, "use not login"),
    USER_ALREADY_REGISTR(204, "user already register"),
    USER_LOGIN_FORM_CONTENT_BLACK(205, "user name or password can't be blank"),



    // 30 start with, about book and deal
    STOCK_NOT_ENOUGH(301, "stock not enough"),
    RATELIMIT(303, "the requests are too much, please try it latter"),
    ITEM_NOT_EXIT(304, "book not exit"),
    BOOK_DATA_FORMAT_ERROR(305, "transform error occur, when transform DO to model"),
    BOOK_NOT_EXIST(306, "book not exist");


    EmBusinessError(int errCode, String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    private int errCode;
    private String errMsg;

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
