package com.example.springwebserver.controller; 

import com.example.springwebserver.service.UserCenterService;
import com.example.springwebserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

/** 
* UserController Tester. 
* 
* @author <Susie>
* @since <pre>6月 9, 2020</pre> 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Slf4j
public class UserControllerTest extends AbstractMvcTest{

/** 
* 
* Method: getUser(@RequestParam(name = "id") Long id) 
* 
*/ 
    @Test
    public void testGetUser() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/user")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .param("id", String.valueOf(22)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

/** 
* 
* Method: register(@RequestParam(name = "userName") String userName, @RequestParam(name = "gender") Boolean gender, @RequestParam(name = "age") Integer age, @RequestParam(name = "password") String password, @RequestParam(name = "introduction", required = false) String introduction) 
* 
*/ 
@Test
public void testRegister() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: EncodeByMd5(String str) 
* 
*/ 
@Test
public void testEncodeByMd5() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: login(@RequestParam(name = "userName") String userName, @RequestParam(name = "password") String password) 
* 
*/ 
    @Test
    public void testLogin() throws Exception {
        String result =
    //       mockMvc.perform(formLogin("/user/login").user("userName","lmy1234").password("password","dawangba1"))
                mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .param("userName","lmy1234")
                        .param("password","dawangba1"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

/** 
* 
* Method: userCenter(@RequestParam(name = "userID") long userID) 
* 
*/ 
    @Test
    public void testUserCenter() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/user/center")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .param("userID", String.valueOf(22)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

/** 
* 
* Method: setWantRead(@RequestParam(name = "bookID") long bookID) 
* 
*/ 
    @Test
    @Transactional
    public void testSetWantRead() throws Exception {
        final String token = extractToken(login("lmy1234", "dawangba1").andReturn());
        log.warn("--------token: "+token);
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/user/wantRead")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization",token)
                .param("bookID", String.valueOf(22)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

/** 
* 
* Method: setHasRead(@RequestParam(name = "bookID") long bookID) 
* 
*/ 
    @Test
    @Transactional
    public void testSetHasRead() throws Exception {
        final String token = extractToken(login("lmy1234", "dawangba1").andReturn());
        log.warn("--------token: "+token);
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/user/hasRead")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization",token)
                .param("bookID", String.valueOf(22)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }


/** 
* 
* Method: convertFromModel(UserModel userModel) 
* 
*/ 
@Test
public void testConvertFromModel() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = UserController.getClass().getMethod("convertFromModel", UserModel.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
}


    public UserControllerTest(){
    }


} 
