package com.example.springwebserver.controller;

import com.example.springwebserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * MallShoppingCartController Tester.
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
public class MallShoppingCartControllerTest extends AbstractMvcTest {
    @Autowired
    private MallShoppingCartController mallShoppingCartController;
    @Autowired
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    public MallShoppingCartControllerTest(){

    }
    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Transactional
    public void testcartListPage() throws Exception {
        final String token = extractToken(login("lmy1234", "dawangba1").andReturn());
        log.warn("--------token: "+token);
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/shopping/my-cart")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization",  token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);

    }
    @Test
    @Transactional
    public void testsaveMallShoppingCartItem() throws Exception {
        final String token = extractToken(login("lmy1234", "dawangba1").andReturn());
        log.warn("--------token: "+token);
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/shopping/add")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization",token)
                .param("bookId", String.valueOf(731))
                .param("count", String.valueOf(20))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }
    @Test
    @Transactional
    public void testupdateMallShoppingCartItem() throws Exception {
        final String token = extractToken(login("lmy1234", "dawangba1").andReturn());
        log.warn("--------token: "+token);
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/shopping/update")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization",token)
                .param("mallShoppingCartItemId", String.valueOf(13))
                .param("goodsCount", String.valueOf(20))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

    @Test
    @Transactional
    public void testdeleteMallShoppingCartItem() throws Exception {
        final String token = extractToken(login("lmy1234", "dawangba1").andReturn());
        log.warn("--------token: "+token);
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/shopping/delete")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization",token)
                .param("mallShoppingCartItemId", String.valueOf(13)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }


}
