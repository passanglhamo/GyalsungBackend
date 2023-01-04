/*
package com.microservice.erp.rest;

import com.microservice.erp.domain.entities.SaUser;
import com.microservice.erp.domain.models.Gender;
import com.microservice.erp.services.ServiceExecutionLogger;
import com.microservice.erp.services.impl.UserInfoService;
import com.microservice.erp.webapp.WebApplicationTest;
import com.microservice.erp.webapp.config.BeanConfig;
import com.microservice.erp.webapp.config.TestJPAH2Config;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.runner.RunWith;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;

*/
/**
 * @author Rajib Kumer Ghosh
 *
 *//*


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebApplicationTest.class
        , UserInfoController.class, UserInfoService.class
        , BeanConfig.class, TestJPAH2Config.class
        , ServiceExecutionLogger.class, AnnotationAwareAspectJAutoProxyCreator.class})
//@TestPropertySource(locations = {"classpath:h2-db.properties"})
public class SaUserControllerIntegrationTest {

    @Value("${app.db.name}")
    private String dbName;

    @Rule
    public final EnvironmentVariables env = new EnvironmentVariables();

    @Before
    public void before() {
        env.set("my.system.env", "my-env");
        env.set("app.db.name", dbName);
    }

    @Test
    public void envTest(){
        Assert.assertTrue(System.getenv("my.system.env").equalsIgnoreCase("my-env"));
    }

    @Autowired
    private UserInfoController controller;

    @Test
    public void initiateTest(){
        System.out.println("Integration Tests");
    }

    @Test
    public void count(){
        SaUser aSaUser = new SaUser();
        aSaUser.setId(new BigInteger("1"));
        //
        controller.insert(aSaUser);
        //
        Integer count = 121;
        System.out.println(count);
    }

    @Test
    public void query(){
        //
        */
/*controller.insert(new SaUser("Rajib The Coder", Gender.MALE, 24));
        controller.insert(new SaUser("Zepa The Pankha Coder", Gender.MALE, 24));
        controller.insert(new SaUser("Passang The Pagla", Gender.MALE, 26));*//*

        //
        int size = Long.valueOf(controller.getRowCount()).intValue();
        //List<SaUser> items = controller.query(size, 0);
        //items.stream().forEach(userInfo -> System.out.println(userInfo.getName()));
    }

}*/
