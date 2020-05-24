package com.example.springwebserver.controller;


import com.example.springwebserver.dataObject.BookDO;
import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.response.CommonReturnType;
import com.example.springwebserver.service.BookRecommendService;
import com.example.springwebserver.service.BookService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("recommend")
@RequestMapping("/recommend")
@Slf4j
@Api(tags = "推荐相关接口", value = "提供个性化推荐相关的 Rest API")
public class RecommendController {
    @Autowired
    private BookRecommendService bookRecommendService;

    @Autowired
    private BookService bookService;

    @GetMapping("/testToken")
    @ResponseBody
    public CommonReturnType testToken() throws BusinessException {

        return CommonReturnType.create(null);

    }

    /**
     * 通过bookID获取相关推荐
     * @param userName
     * @return
     * @throws BusinessException
     */
    @GetMapping("/book")
    @ResponseBody
    public CommonReturnType getRecommendByBookID(@RequestParam(name = "page") int page,
                                                 @RequestParam(name = "size") int size,
                                                 @RequestParam(name = "bookID") Long bookID) throws BusinessException {
        return CommonReturnType.create(bookRecommendService.listBookRecommendByBookID(bookID,page,size));
    }
}
