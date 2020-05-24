package com.example.springwebserver.service.model;

import lombok.Data;

@Data
public class ReviewModel {
    private String bookID;
    private String bookName;
    private String coverUrl;
    private String content;
    private String reviewTime;
}
