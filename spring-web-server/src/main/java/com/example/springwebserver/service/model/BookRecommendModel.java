package com.example.springwebserver.service.model;


import lombok.Data;
import org.joda.time.DateTime;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class BookRecommendModel implements Serializable {
    private Long bookId;
    private List<BookModel> recommends;
}
