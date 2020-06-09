package com.example.springwebserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
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

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
/**
 * ReviewController Tester.
 *
 * @author <Susie>
 * @since <pre>6月 9, 2020</pre>
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
@WebAppConfiguration
@Slf4j
public class ReviewControllerTest extends AbstractMvcTest{

    @Test
    @Transactional
    public void addReview() throws Exception {
        final String token = extractToken(login("lmy1234", "dawangba1").andReturn());
        log.warn("--------token: "+token);
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/review/add")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .param("bookId", String.valueOf(22))
                .param("content","这本书好好看哦")
                .param("star","5")
                .header("Authorization",  token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{'status':true}"))
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

    @Test
    @Transactional
    public void delReview() throws Exception {
        final String token = extractToken(login("lmy1234", "dawangba1").andReturn());
        log.warn("--------token: "+token);
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/review/delete")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization",  token)
                .param("reviewId", String.valueOf(1022)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{'status':true}"))
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

    @Test
    @Transactional
    public void agreeReview() throws Exception {
        final String token = extractToken(login("lmy1234", "dawangba1").andReturn());
        log.warn("--------token: "+token);
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/review/agree")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .param("reviewId", String.valueOf(1022))
                .header("Authorization",  token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{'status':true}"))
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

    @Test
    @Transactional
    public void getReviewByBookIdDescAgreeNum() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/review/book/agreeNum")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .param("bookId", String.valueOf(22))
                .param("page","1")
                .param("size","10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{'status':true}"))
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

    @Test
    @Transactional
    public void getReviewByBookIdDescByTime() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/review/book/reviewTime")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .param("bookId", String.valueOf(22))
                .param("page","1")
                .param("size","10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{'status':true}"))
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }
}