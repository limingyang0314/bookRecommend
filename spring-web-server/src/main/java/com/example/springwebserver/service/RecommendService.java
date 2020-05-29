package com.example.springwebserver.service;

import com.example.springwebserver.dataObject.TagDO;
import com.example.springwebserver.service.model.BookModel;
import com.example.springwebserver.service.model.BookRecommendModel;

import java.util.List;

public interface RecommendService {
    List<BookModel> listBookRecommendByBookID(Long bookID, int page, int size);

    List<BookModel> listBookRecommendByUserID(Long userID, int page, int size);

    List<TagDO> listTagRecommendByUserID(Long userID);
}
