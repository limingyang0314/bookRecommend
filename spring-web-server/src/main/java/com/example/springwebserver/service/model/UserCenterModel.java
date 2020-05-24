package com.example.springwebserver.service.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserCenterModel  implements Serializable {
    private UserModel user;
    private List<BookModel> wantRead;
    private List<BookModel> hasRead;
    private List<ReviewModel> reviews;
}
