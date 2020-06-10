package com.example.springwebserver.controller;

import com.example.springwebserver.dao.*;
import com.example.springwebserver.dataObject.BookDO;
import com.example.springwebserver.dataObject.RatingDO;
import com.example.springwebserver.dataObject.ReviewDO;
import com.example.springwebserver.dataObject.UserDO;
import com.example.springwebserver.enums.EmBusinessError;
import com.example.springwebserver.exception.BusinessException;
import com.example.springwebserver.response.CommonReturnType;
import com.example.springwebserver.service.MallOrderService;
import com.example.springwebserver.service.UserService;
import com.example.springwebserver.service.model.UserModel;
import com.example.springwebserver.util.PageQueryUtil;
import com.example.springwebserver.util.PageResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller("admin")
@RequestMapping("/admin")
@Slf4j
@Api(tags = "管理后台接口", value = "提供基础后台管理的 Rest API")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private AdminDOMapper adminDOMapper;

    @Autowired
    private BookDOMapper bookDOMapper;

    @Autowired
    private ReviewDOMapper reviewDOMapper;

    @Autowired
    private RatingDOMapper ratingDOMapper;
    @Autowired
    private MallOrderService mallOrderService;

    /**
     * 未登录或者非管理员将无法使用管理接口
     * @return
     * @throws BusinessException
     */
    private boolean judgeAdmin() throws BusinessException {
        if(!userService.isLoginUser()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
            //return false;
        }
        UserModel user = userService.getUserByToken();
        if(adminDOMapper.selectByPrimaryKey(user.getUserId()) == null){

            return false;
        }
        return true;
    }

    @ApiOperation("判断是否为管理员")
    @GetMapping("/isAdmin")
    @ResponseBody
    public CommonReturnType isAdmin() throws BusinessException {
        HashMap<String,String> ret = new HashMap<>();
        if(!userService.isLoginUser()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        UserModel user = userService.getUserByToken();
        if(adminDOMapper.selectByPrimaryKey(user.getUserId()) == null){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        ret.put("message","Access right.");
        return CommonReturnType.create(ret);
    }

    @ApiOperation("删除书籍(软删除)")
    @GetMapping("/deleteBook")
    @ResponseBody
    public CommonReturnType deleteBook(@RequestParam(name = "bookID") long bookId) throws BusinessException {
        HashMap<String,String> ret = new HashMap<>();
        if(!judgeAdmin()){
            throw new BusinessException(EmBusinessError.NOT_ADMIN_USER);
        }
        bookDOMapper.softDeleteByPrimaryKey(bookId);

        ret.put("message","Delete success.");
        return CommonReturnType.create(ret);
    }

    @ApiOperation("删除评论(软删除)")
    @GetMapping("/deleteReview")
    @ResponseBody
    public CommonReturnType deleteReview(@RequestParam(name = "reviewID") long reviewId) throws BusinessException {
        HashMap<String,String> ret = new HashMap<>();
        if(!judgeAdmin()){
            throw new BusinessException(EmBusinessError.NOT_ADMIN_USER);
        }
        reviewDOMapper.softDeleteByPrimaryKey(reviewId);

        ret.put("message","Delete success.");
        return CommonReturnType.create(ret);
    }

    @ApiOperation("为某本书随机生成若干评分")
    @GetMapping("/randomRating")
    @ResponseBody
    public CommonReturnType randomRating(@RequestParam(name = "bookID") long bookId,
                                         @RequestParam(name = "minRating") int min,
                                         @RequestParam(name = "maxRating") int max,
                                         @RequestParam(name = "size") int size) throws BusinessException {
        HashMap<String,String> ret = new HashMap<>();
        if(!judgeAdmin()){
            throw new BusinessException(EmBusinessError.NOT_ADMIN_USER);
        }
        for(int i = 0 ; i < size ; ++ i){
            Random random = new Random();
            Integer temp_rating = min + (int)random.nextInt(max - min + 1);
            //找一个没有评论这本书的用户
            UserDO user = userDOMapper.selectOneUserNotRatingTheBook(bookId);
            RatingDO rating = new RatingDO();
            rating.setRating(temp_rating.doubleValue());
            rating.setBookId(bookId);
            rating.setUserId(user.getUserId());
            ratingDOMapper.insertSelective(rating);
        }
        ret.put("message","Insert success.");
        return CommonReturnType.create(ret);
    }

    @ApiOperation("用户分页列表")
    @GetMapping("/userList")
    @ResponseBody
    public CommonReturnType userList(@RequestParam(name = "page") int page,
                                         @RequestParam(name = "size") int size) throws BusinessException {
        PageHelper.startPage(page, size);
        Page<UserDO> users = userDOMapper.listUserByPage();
        List<UserDO> userList = users.getResult();
        return CommonReturnType.create(userList);
    }

    @ApiOperation("删除用户(软删除)")
    @GetMapping("/deleteUser")
    @ResponseBody
    public CommonReturnType deleteUser(@RequestParam(name = "userID") long userId) throws BusinessException {
        HashMap<String,String> ret = new HashMap<>();
        if(!judgeAdmin()){
            throw new BusinessException(EmBusinessError.NOT_ADMIN_USER);
        }
        userDOMapper.softDeleteByPrimaryKey(userId);

        ret.put("message","Delete success.");
        return CommonReturnType.create(ret);
    }

    @ApiOperation("分页获取订单列表")
    @GetMapping("/order")
    @ResponseBody
    public CommonReturnType orderListPage() throws BusinessException {
        Map<String, Object> params = new HashMap<>();
        if(!judgeAdmin()){
            throw new BusinessException(EmBusinessError.NOT_ADMIN_USER);
        }
        if (StringUtils.isEmpty(params.get("page"))) {
            params.put("page", 1);
        }
        params.put("limit", 20);
        //封装订单数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        PageResult pageResult = mallOrderService.getMyOrders(pageUtil);
        if(pageResult==null){
            log.warn("==== [get order] ==== order not exit");
            throw new BusinessException(EmBusinessError.MYORDER_NOT_EXIST);
        }
        return CommonReturnType.create(pageResult);
    }

    @ApiOperation("获取订单总数")
    @GetMapping("/order/count")
    @ResponseBody
    public CommonReturnType orderCount() throws BusinessException {
        Map<String, Object> ret = new HashMap<>();
        if(!judgeAdmin()){
            throw new BusinessException(EmBusinessError.NOT_ADMIN_USER);
        }

        int count = mallOrderService.getOrderCount();
        if(count==0){
            log.warn("==== [get order] ==== order not exit");
            throw new BusinessException(EmBusinessError.MYORDER_NOT_EXIST);
        }
        ret.put("message",count+"");
        return CommonReturnType.create(ret);
    }

}
