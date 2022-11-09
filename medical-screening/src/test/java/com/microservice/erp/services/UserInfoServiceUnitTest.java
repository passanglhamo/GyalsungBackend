//package com.microservice.erp.services;
//
//import com.microservice.erp.domain.entities.UserInfo;
//import com.microservice.erp.domain.models.Gender;
//import com.microservice.erp.domain.repositories.IUserInfoRepository;
//import com.microservice.erp.services.iServices.IUserInfoService;
//import com.microservice.erp.services.impl.UserInfoService;
//import com.microservice.erp.webapp.config.TestJPAH2Config;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {TestJPAH2Config.class})
//public class UserInfoServiceUnitTest {
//
//    @Before
//    public void setup(){
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Mock
//    IUserInfoRepository repository;
//
//    @InjectMocks
//    IUserInfoService service = new UserInfoService(repository);
//
//    @Test
//    public void happyPathTest(){
//        //Defining Mock Object:
//        UserInfo aUserInfo = new UserInfo("Rajib", Gender.MALE, 36);
//        when(repository.save(any(UserInfo.class))).thenReturn(aUserInfo);
//
//        //Call controller to make the save:
//        UserInfo nUserInfo = service.add(aUserInfo);
//
//        //Verify:
//        assertNotNull(nUserInfo);
//        assertNotNull(nUserInfo.getId());
//        assertEquals("Rajib", nUserInfo.getName());
//        //System.out.println(nUserInfo.marshallingToMap(true));
//    }
//}
