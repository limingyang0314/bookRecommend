package com.example.springwebserver.controller; 

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
/**
* RecommendController Tester. 
* 
* @author <Susie>
* @since <pre>6月 9, 2020</pre> 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Slf4j
public class RecommendControllerTest extends AbstractMvcTest {


/** 
* 
* Method: getRecommendByBookID(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size, @RequestParam(name = "bookID") Long bookID) 
* 
*/ 
    @Test
    public void testGetRecommendByBookID() throws Exception {
    //TODO: Test goes here...
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/recommend/book")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .param("page", String.valueOf(1))
                .param("size",10+"")
                .param("bookID",20+""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{'status':true}"))
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

/** 
* 
* Method: getRecommendByUserID(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size, @RequestParam(name = "userID") Long userID) 
* 
*/ 
    @Test
    public void testGetRecommendByUserID() throws Exception {
    //TODO: Test goes here...
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/recommend/user")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .param("userID", String.valueOf(22))
                .param("page",1+"")
                .param("size",10+""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{'status':true}"))
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

/** 
* 
* Method: getHotRank(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) 
* 
*/ 
    @Test
    public void testGetHotRank() throws Exception {
    //TODO: Test goes here...
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/recommend/hotRank")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .param("page", String.valueOf(1))
                .param("size","10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{'status':true}"))
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

/** 
* 
* Method: getRecommendTagByUserID(@RequestParam(name = "userID") Long userID) 
* 
*/ 
    @Test
    public void testGetRecommendTagByUserID() throws Exception {
    //TODO: Test goes here...
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/recommend/userTag")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .param("userID", String.valueOf(22)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{'status':true}"))
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

/** 
* 
* Method: getHotTag(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) 
* 
*/ 
    @Test
    public void testGetHotTag() throws Exception {
    //TODO: Test goes here...
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/recommend/hotTag")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .param("page", String.valueOf(1))
                .param("size","10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{'status':true}"))
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }


} 
