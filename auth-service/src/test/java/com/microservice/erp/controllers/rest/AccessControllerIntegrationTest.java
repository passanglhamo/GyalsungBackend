//package com.microservice.erp.controllers.rest;
//
//import com.microservice.erp.controllers.rest.AccessController;
//import com.microservice.erp.domain.entities.Statement;
//import com.microservice.erp.domain.models.AccessPermission;
//import com.microservice.erp.domain.models.Action;
//import com.microservice.erp.webapp.JwtAuthApiTest;
//import com.microservice.erp.webapp.config.BeanConfig;
//import com.microservice.erp.webapp.config.JWTokenValidator;
//import com.microservice.erp.webapp.config.SecurityConfig;
//import com.microservice.erp.webapp.config.TestJPAConfig;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.env.Environment;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE
//        , classes = {JwtAuthApiTest.class, BeanConfig.class, SecurityConfig.class, JWTokenValidator.class, TestJPAConfig.class})
//@TestPropertySource(locations = {"classpath:application.properties", "classpath:application-mysql.properties"})
//public class AccessControllerIntegrationTest {
//
//    @Before
//    public void setUp() throws Exception {
//        System.out.println(env.getProperty("spring.application.name"));
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }
//
//    @Autowired
//    private Environment env;
//
//    @Autowired
//    private AccessController controller;
//
//    @Test
//    public void permissionTest(){
//        AccessPermission permission = new AccessPermission();
//        Statement ss = new Statement();
//        ss.setResource("app_access");
//        permission.setStatement(ss);
//        permission.setUsername("Test_TMR2");
//        //
//        ResponseEntity<AccessPermission> res = controller.hasPermission(permission);
//        System.out.println(res.toString());
//        AccessPermission response = res.getBody();
//        Assert.assertTrue("permissionTest Not Success", response.getStatus() == 200);
//        Assert.assertTrue("permissionTest Not Success", response.getStatement().getAction() == Action.Write);
//    }
//
//    @Test
//    public void permissionFailedTest(){
//        AccessPermission permission = new AccessPermission();
//        Statement ss = new Statement();
//        ss.setResource("manage_uddoktas");
//        permission.setStatement(ss);
//        permission.setUsername("Test_TMR2");
//        //
//        ResponseEntity<AccessPermission> res = controller.hasPermission(permission);
//        System.out.println(res.toString());
//        AccessPermission response = res.getBody();
//        Assert.assertTrue("permissionTest Not Success", response.getStatus() == 401);
//        Assert.assertTrue("permissionTest Not Success", response.getStatement().getAction() == Action.None);
//    }
//
//    @Test
//    public void permissionFailed2ndTest(){
//        AccessPermission permission = new AccessPermission();
//        Statement ss = new Statement();
//        ss.setResource("manage_uddoktas");
//        permission.setStatement(ss);
//        //
//        ResponseEntity<AccessPermission> res = controller.hasPermission(permission);
//        System.out.println(res.toString());
//        AccessPermission response = res.getBody();
//        Assert.assertTrue("permissionTest Not Success", response.getStatus() == 401);
//    }
//
//    @Test
//    public void permission2ndTest(){
//        AccessPermission permission = new AccessPermission();
//        Statement ss = new Statement();
//        ss.setResource("admin_access");
//        permission.setStatement(ss);
//        permission.setUsername("01704161202");
//        //
//        ResponseEntity<AccessPermission> res = controller.hasPermission(permission);
//        System.out.println(res.toString());
//        AccessPermission response = res.getBody();
//        Assert.assertTrue("permission2ndTest Not Success", response.getStatus() == 200);
//        Assert.assertTrue("permission2ndTest Not Success", response.getStatement().getAction() == Action.Write);
//    }
//
//    @Test
//    public void permission3ndTest(){
//        AccessPermission permission = new AccessPermission();
//        Statement ss = new Statement();
//        ss.setResource("add_uddokta");
//        permission.setStatement(ss);
//        permission.setUsername("01704161202");
//        //
//        ResponseEntity<AccessPermission> res = controller.hasPermission(permission);
//        System.out.println(res.toString());
//        AccessPermission response = res.getBody();
//        Assert.assertTrue("permission3ndTest Not Success", response.getStatus() == 200);
//        Assert.assertTrue("permission3ndTest Not Success", response.getStatement().getAction() == Action.Write);
//    }
//}