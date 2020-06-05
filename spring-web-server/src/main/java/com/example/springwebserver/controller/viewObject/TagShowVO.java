package com.example.springwebserver.controller.viewObject;

import com.example.springwebserver.service.model.BookModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@ApiModel("标签展示实体")
public class TagShowVO implements Serializable {
    private String tagName;

    private Integer tagId;

    private Long tagHot;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<BookModel> hotBooks;

    public void setHotBooks(List<BookModel> hotBooks){ this.hotBooks = hotBooks; }

    public List<BookModel> getHotBooks(){ return this.hotBooks; }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName == null ? null : tagName.trim();
    }

    public Long getTagHot() {
        return tagHot;
    }

    public void setTagHot(Long tagHot) {
        this.tagHot = tagHot;
    }
}
