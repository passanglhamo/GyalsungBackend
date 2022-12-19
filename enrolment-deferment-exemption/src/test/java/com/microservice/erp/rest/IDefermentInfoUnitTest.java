package com.microservice.erp.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.erp.controllers.rest.DefermentController;
import com.microservice.erp.services.iServices.ICreateDefermentService;
import com.microservice.erp.webapp.WebApplicationTest;
import com.microservice.erp.webapp.config.BeanConfig;
import com.microservice.erp.webapp.config.TestJPAH2Config;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = {WebApplicationTest.class, TestJPAH2Config.class, BeanConfig.class})
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:h2-db.properties")
public class IDefermentInfoUnitTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ICreateDefermentService service;

    @InjectMocks
    private DefermentController controller;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);

    }


    @Test
    public void happyTest() throws Exception {

        //Defining Mock Object:
        HttpServletRequest request = null;
        ICreateDefermentService.CreateDefermentCommand createCommand =
                new ICreateDefermentService.CreateDefermentCommand(
                        null,
                        new BigInteger("1"),
                        new BigInteger("1"),
                        null,
                        new Date(),
                        'P',
                        null,
                        null
                );

        // when(service.saveDeferment(request,createCommand)).thenReturn(createCommand);

        //Call controller to make the save:
        MvcResult result = mockMvc.perform(post("/deferment")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(createCommand))
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                //.andExpect(status().isOk())
                .andReturn();
        String str = "Status: " + result.getResponse().getStatus();
        System.out.println(str);
        //

//        controller.saveDeferment(request,createCommand);
//        System.out.print(controller.getAllDefermentList(null));
//        Assert.assertEquals(controller.getAllDefermentList(null).size(),1);

    }
}
