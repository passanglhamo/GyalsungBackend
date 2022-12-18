package com.microservice.erp.services;

import com.microservice.erp.domain.entities.SaUser;
import com.microservice.erp.domain.models.Gender;
import com.microservice.erp.domain.repositories.ISaUserRepository;
import com.microservice.erp.services.iServices.IUserInfoService;
import com.microservice.erp.services.impl.UserInfoService;
import com.microservice.erp.webapp.config.TestJPAH2Config;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {TestJPAH2Config.class})
public class SaUserServiceUnitTest {

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    ISaUserRepository repository;

    @InjectMocks
    IUserInfoService service = new UserInfoService(repository);

    @Test
    public void happyPathTest(){
        //Defining Mock Object:
        //SaUser aSaUser = new SaUser("Rajib", Gender.MALE, 36);
        SaUser aSaUser = new SaUser();
        aSaUser.setId(new BigInteger("1"));
        when(repository.save(any(SaUser.class))).thenReturn(aSaUser);

        //Call controller to make the save:
        SaUser nSaUser = service.add(aSaUser);

        //Verify:
        assertNotNull(nSaUser);
        assertNotNull(nSaUser.getId());
        assertEquals("Rajib", nSaUser.getId());
        //System.out.println(nSaUser.marshallingToMap(true));
    }
}