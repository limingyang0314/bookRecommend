package com.example.springwebserver.service.model;


import lombok.Data;
import org.joda.time.DateTime;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class BookModel implements Serializable {
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

    @NotBlank(message = "ISBN can't be blank")
    private String isbn;

    @NotBlank(message = "book name can't be blank")
    private String bookName;

    private String bookSubname;

    @NotNull(message = "authorId can't be blank")
    private Long authorId;

    @NotNull(message = "countryId can't be blank")
    private Integer countryId;

    @NotBlank(message = "publisher can't be blank")
    private String publisher;

    @NotBlank(message = "publishedPlace can't be blank")
    private String publishedPlace;

    private String publishedTime;

    @NotNull(message = "tagIds can't be blank")
    @Size(min = 1, message = "a boos has at least one tag")
    private List<String> tagIds;

    @NotNull(message = "page can't be blank")
    @Min(value = 1, message = "age can't less than 1")
    private Integer page;

    @NotNull(message = "price can't be blank")
    private BigDecimal price;

    private List<Seller> sellerlist;

    @NotBlank(message = "coverUrl can't be blank")
    private String coverUrl;

    @NotBlank(message = "description can't be blank")
    private String description;

    @NotNull(message = "rating can't be blank")
    @Min(value = 0, message = "rating can't less than 0")
    @Max(value = 10, message = "rating can't large than 10")
    private Integer rating;
}
