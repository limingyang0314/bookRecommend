package com.example.springwebserver.controller.viewObject;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.joda.time.DateTime;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;


@Data
@ApiModel("图书实体")
public class BookVO {
    private Long bookId;

    private String isbn;

    private String bookName;

    private String bookSubname;

    private Long authorId;

    private Integer countryId;

    private String publisher;

    private String publishedPlace;

    private DateTime publishedTime;

    private List<String> tagIds;

    private Integer page;

    private BigDecimal price;

    private List<String> sellerlist;

    private String coverUrl;

    private String description;

    private Integer rating;
}
