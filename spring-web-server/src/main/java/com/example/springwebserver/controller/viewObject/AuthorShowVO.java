package com.example.springwebserver.controller.viewObject;

import com.example.springwebserver.service.model.BookModel;

import java.io.Serializable;
import java.util.List;

public class AuthorShowVO implements Serializable {

    private Long authorId;

    private String authorName;

    private Integer countryId;

    private Long author_hot;

    private List<BookModel> hotBooks;

    public void setHotBooks(List<BookModel> books){
        hotBooks = books;
    }

    public List<BookModel> getHotBooks(){
        return hotBooks;
    }

    public void setAuthor_hot(Long author_hot){ this.author_hot = author_hot; };

    public Long getAuthor_hot() {
        return author_hot;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName == null ? null : authorName.trim();
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }
}
