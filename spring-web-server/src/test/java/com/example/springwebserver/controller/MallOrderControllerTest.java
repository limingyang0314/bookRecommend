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

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/** 
* MallOrderController Tester. 
* 
* @author <Susie>
* @since <pre>6月 9, 2020</pre> 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Slf4j
public class MallOrderControllerTest extends AbstractMvcTest {


/** 
* 
* Method: orderDetailPage(@RequestParam(name = "orderNo") String orderNo) 
* 
*/ 
    @Test
    public void testOrderDetailPage() throws Exception {
        final String token = extractToken(login("lmy1234", "dawangba1").andReturn());
        log.warn("--------token: "+token);
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/order/detail")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization",token)
                .param("orderNo","15908251405582642"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{'status':true}"))
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

/** 
* 
* Method: orderListPage() 
* 
*/ 
    @Test
    public void testOrderListPage() throws Exception {
        final String token = extractToken(login("lmy1234", "dawangba1").andReturn());
        log.warn("--------token: "+token);
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/order/history")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization",token)
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
* Method: cancelOrder(@RequestParam (name = "orderNo") String orderNo) 
* 
*/ 
    @Test
    @Transactional
    public void testCancelOrder() throws Exception {
        final String token = extractToken(login("lmy1234", "dawangba1").andReturn());
        log.warn("--------token: "+token);
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/order/cancel")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization",token)
                .param("orderNo", "15908251405582642"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"SUCCESS\"}"))
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

/** 
* 
* Method: finishOrder(@RequestParam (name = "orderNo") String orderNo) 
* 
*/ 
    @Test
    @Transactional
    public void testFinishOrder() throws Exception {
        final String token = extractToken(login("lmy1234", "dawangba1").andReturn());
        log.warn("--------token: "+token);
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/order/finish")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization",token)
                .param("orderNo", "15908251405582642"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"SUCCESS\"}"))
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }

/** 
* 
* Method: saveOrder(@RequestParam(value = "itemIds[]") Long[] itemIds) 
* 
*/ 
    @Test
    @Transactional
    public void testSaveOrder() throws Exception {
        final String token = extractToken(login("lmy1234", "dawangba1").andReturn());
        log.warn("--------token: "+token);
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/order/create")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .header("Authorization",token)
                .param("itemIds[]", "36, 37"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{\"status\":true}"))
                .andReturn().getResponse().getContentAsString();
        log.warn("--------返回的json = "+result);
    }


} 
