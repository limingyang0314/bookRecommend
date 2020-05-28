package com.example.springwebserver.controller.viewObject;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.joda.time.DateTime;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@Data
@ApiModel("图书实体")
public class BookVO implements Serializable {

    @Data
    public static class Seller implements Serializable {
        String place;
        BigDecimal price;

        public Seller(String place, BigDecimal price) {
            this.place = place;
            this.price = price;
        }
    }

    private Long bookId;

    private String isbn;

    private String bookName;

    private String bookSubname;

    private Long authorId;

    private Integer countryId;

    private String authorName;

    private String countryName;

    private String publisher;

    private String publishedPlace;

    private DateTime publishedTime;

    private List<String> tagIds;

    private Integer page;

    private BigDecimal price;

    private List<Seller> sellerlist;

    private String coverUrl;

    private String description;

    private double rating;

    private boolean hasRead = false;

    private boolean wantRead = false;

    private double myRating = 0;
}
