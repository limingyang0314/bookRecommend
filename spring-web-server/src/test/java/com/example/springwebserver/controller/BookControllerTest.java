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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/** 
* BookController Tester. 
* 
* @author <Susie>
* @since <pre>6月 9, 2020</pre> 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Slf4j
public class BookControllerTest extends AbstractMvcTest {


/** 
* 
* Method: getBookByPage(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) 
* 
*/ 
    @Test
    public void testGetBookByPage() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/book/list")
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
* Method: getBookByAuthorPage(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size, @RequestParam(name = "author_ID") String author_ID) 
* 
*/ 
    @Test
    public void testGetBookByAuthorPage() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/book/author")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .param("page", String.valueOf(1))
                .param("size","10")
                .param("author_ID","22"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{'status':true}"))
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

/** 
* 
* Method: getBookByTagPage(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size, @RequestParam(name = "tag_ID") String tag_ID) 
* 
*/ 
    @Test
    public void testGetBookByTagPage() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/book/tag")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .param("page", String.valueOf(1))
                .param("size","10")
                .param("tag_ID","15"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{'status':true}"))
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

/** 
* 
* Method: getBookByBookID(@RequestParam(name = "bookID") long bookId) 
* 
*/ 
    @Test
    public void testGetBookByBookID() throws Exception {
        final String token = extractToken(login("lmy1234", "dawangba1").andReturn());
        log.warn("--------token: "+token);
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/book")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .param("bookID", String.valueOf(999))
                .header("Authorization",token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{'status':true}"))
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

/** 
* 
* Method: ratingBook(@RequestParam(name = "bookID") long bookId, @RequestParam(name = "rating") double ratingNum) 
* 
*/ 
    @Test
    @Transactional
    public void testRatingBook() throws Exception {
        final String token = extractToken(login("lmy1234", "dawangba1").andReturn());
        log.warn("--------token: "+token);
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/book/rating")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .param("bookID", "99")
                .param("rating","2")
                .header("Authorization",token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{'status':true}"))
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }


} 
