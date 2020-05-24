package com.example.springwebserver.service;

import com.example.springwebserver.service.model.UserCenterModel;

public interface UserCenterService {
    public UserCenterModel getUserCenterByUserID(Long userID);
}
