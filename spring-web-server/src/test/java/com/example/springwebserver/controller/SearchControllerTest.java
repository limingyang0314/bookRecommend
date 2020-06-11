package com.example.springwebserver.controller;


import com.example.springwebserver.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

/**
 * SearchController Tester.
 *
 * @author <Susie>
 * @since <pre>6月 9, 2020</pre>
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Slf4j
public class SearchControllerTest extends AbstractMvcTest {
    @Autowired
    private SearchService searchService;

    public SearchControllerTest(){}

    @Test
    @Transactional
    public void testsearchByKey() throws Exception {

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/search")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .param("key", String.valueOf(22))
                .param("page",1+"")
                .param("size",1+""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }
}
