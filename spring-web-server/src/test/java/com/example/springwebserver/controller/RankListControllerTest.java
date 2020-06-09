package com.example.springwebserver.controller; 

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
/** 
* RankListController Tester. 
* 
* @author <Susie>
* @since <pre>6月 9, 2020</pre> 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Slf4j
public class RankListControllerTest extends AbstractMvcTest {


/** 
* 
* Method: getHotRank(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) 
* 
*/ 
    @Test
    public void testGetHotRank() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/rankList/hotRank")
                        .param("page","1")
                        .param("size","10"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(print())
                        .andExpect(MockMvcResultMatchers.content().json("{'status':true}"))
                        .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

/** 
* 
* Method: getHotTagAndBook(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) 
* 
*/ 
    @Test
    public void testGetHotTagAndBook() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/rankList/hotTagAndBook")
                .param("page","1")
                .param("size","10"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{'status':true}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

/** 
* 
* Method: getHotAuthorAndBook(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) 
* 
*/ 
    @Test
    public void testGetHotAuthorAndBook() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/rankList/hotAuthorAndBook")
                .param("page","1")
                .param("size","10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{'status':true}"))
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

/** 
* 
* Method: getHotChineseBook() 
* 
*/ 
    @Test
    public void testGetHotChineseBook() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/rankList/hotChineseBook"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{'status':true}"))
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

/** 
* 
* Method: getHotBoardBook() 
* 
*/ 
@Test
public void testGetHotBoardBook() throws Exception { 
//TODO: Test goes here... 
} 


} 
