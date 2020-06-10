package com.example.springwebserver.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.mysql.cj.xdevapi.JsonArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
@SpringBootTest
@RunWith(SpringRunner.class)
@Ignore
public class AbstractMvcTest {
    protected MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();
    private static Set<Class> inited = new HashSet<>();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Before
    public void init() throws Exception {
        if (!inited.contains(getClass())) {
            doInit();
            inited.add(getClass());
        }
    }
    @After
    public void after() throws Exception {
    }

    protected void doInit() throws Exception {
    }

    protected String json(Object o) throws IOException {
        return mapper.writeValueAsString(o);
    }

    protected ResultActions login(String username, String password) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .param("userName",username)
                        .param("password",password)
                        .contentType(MediaType.APPLICATION_JSON));
    }

    protected String extractToken(MvcResult result) throws UnsupportedEncodingException {
        return JsonPath.read(result.getResponse().getContentAsString(), "$.data.token");
    }

}