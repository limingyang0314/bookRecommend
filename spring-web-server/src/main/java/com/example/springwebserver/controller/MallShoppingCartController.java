package com.example.springwebserver.controller;

import com.example.springwebserver.controller.viewObject.MallShoppingCartItemVO;
import com.example.springwebserver.dataObject.BookDO;
import com.example.springwebserver.dataObject.MallShoppingCartItemDO;
import com.example.springwebserver.enums.EmBusinessError;
import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.response.CommonReturnType;
import com.example.springwebserver.service.BookService;
import com.example.springwebserver.service.MallShoppingCartService;
import com.example.springwebserver.service.UserService;
import com.example.springwebserver.service.model.BookModel;
import com.example.springwebserver.service.model.UserModel;
import com.example.springwebserver.util.Constants;
import com.example.springwebserver.util.PageQueryUtil;
import com.example.springwebserver.util.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("shopping")
@RequestMapping("/shopping")
@Slf4j
@Api(tags = "购物车相关接口", value = "提供购物车相关的 Rest API")
public class MallShoppingCartController {
    @Autowired
    private MallShoppingCartService mallShoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;
   
    @ApiOperation("获取我的购物车信息")
    @GetMapping("/my-cart")
    @ResponseBody
    public CommonReturnType cartListPage() throws BusinessException {
        UserModel user = userService.getUserByToken();
        List<MallShoppingCartItemVO> mallShoppingCartItemVOList = mallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if(mallShoppingCartItemVOList.isEmpty()){
            log.warn("==== [get my-cart] ==== my-cart not exit");
            throw new BusinessException(EmBusinessError.SHOPPING_ITEM_ERROR);
        }
        return CommonReturnType.create(mallShoppingCartItemVOList);
    }

    @ApiOperation("添加图书到购物车")
    @PostMapping("/add")
    @ResponseBody
    public CommonReturnType saveMallShoppingCartItem(@RequestBody Map<String,Long> map) throws BusinessException {
        UserModel user = userService.getUserByToken();
        List<Long> itemIds = new ArrayList<Long>(map.values());
        Map<BookModel,Integer> bookmap = new HashMap<>();
        for(Long itemId:itemIds) {
            BookModel book = bookService.getBookById(itemId);
            bookmap.put(book,bookmap.getOrDefault(book,0)+1);
        }
        List<MallShoppingCartItemDO> mallShoppingCartItemDOS = new ArrayList();
        for(BookModel book:bookmap.keySet()){
            MallShoppingCartItemDO mallShoppingCartItemDO = new MallShoppingCartItemDO();
            mallShoppingCartItemDO.setGoodsCount(bookmap.get(book));
            mallShoppingCartItemDO.setUserId(user.getUserId());
            mallShoppingCartItemDO.setGoodsId(book.getBookId());
            mallShoppingCartItemDO.setIsDeleted((byte)0);
            mallShoppingCartItemDOS.add(mallShoppingCartItemDO);
        }
        //todo 判断数量
        List<MallShoppingCartItemVO> mallShoppingCartItemVOS = mallShoppingCartService.saveMallCartItems(mallShoppingCartItemDOS);

        return CommonReturnType.create(mallShoppingCartItemVOS);
    }
    @ApiOperation("更新购物车商品信息")
    @PostMapping("/update")
    @ResponseBody
    public CommonReturnType updateMallShoppingCartItem(@RequestBody MallShoppingCartItemDO mallShoppingCartItemDO) throws BusinessException {
        UserModel user = userService.getUserByToken();
        MallShoppingCartItemDO mallShoppingCartItemDOUpdate = mallShoppingCartService.updateMallCartItem(mallShoppingCartItemDO);
        return CommonReturnType.create(mallShoppingCartItemDO);
    }

    @ApiOperation("删除购物车商品")
    @PostMapping("/delete")
    @ResponseBody
    public CommonReturnType deleteMallShoppingCartItem(@RequestParam(name = "mallShoppingCartItemId") long mallShoppingCartItemId) throws BusinessException {
        UserModel user = userService.getUserByToken();
        Boolean delete_result = mallShoppingCartService.deleteById(mallShoppingCartItemId);
        return CommonReturnType.create(delete_result);
    }

}
