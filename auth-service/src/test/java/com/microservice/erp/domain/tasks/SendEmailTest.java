//package com.microservice.erp.domain.tasks;
//
//import com.microservice.erp.domain.tasks.sender.SendEmail;
//import com.infoworks.lab.rest.models.Response;
//import com.it.soul.lab.sql.query.models.Row;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.client.RestTemplate;
//
//public class SendEmailTest {
//
//    @Before
//    public void setUp() throws Exception {
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }
//
//    @Test
//    public void sendTest() {
//        RestTemplate notifyTemplate = new RestTemplateBuilder()
//                .rootUri("http://localhost:8082/api/notify/v1")
//                .build();
//        //
//        Row properties = new Row()
//                .add("name", "Kona KSL")
//                .add("subscriptionDate", "2022-06-21")
//                .add("technologies", new String[]{"SpringBoot", "Java", "Docker", "K8s"});
//        //
//        SendEmail emailTask = new SendEmail(notifyTemplate
//                , "noreply@info.com.bd", "receiver@info.com.bd"
//                , "Greetings Message!", "welcome-email-sample.html"
//                , properties);
//        Response response = emailTask.execute(null);
//        Assert.assertTrue(response.getStatus() == HttpStatus.OK.value());
//    }
//}