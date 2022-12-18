package com.microservice.erp.repositories;

import com.microservice.erp.domain.entities.DefermentInfo;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.webapp.config.TestJPAH2Config;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestJPAH2Config.class})
@Transactional
@TestPropertySource(locations = {"classpath:h2-db.properties"})
public class IDefermentInfoRepositoryUnitTest {

    @Autowired
    IDefermentInfoRepository repository;

    /*@Test
    public void insert(){
        DefermentInfo defermentInfo =
                new DefermentInfo(
                        new BigInteger("1"),
                        null,
                        new Date(),
                        new BigInteger("1"),
                        null,
                        null,
                        'P',
                        null);
        repository.save(defermentInfo);

        Assert.assertEquals(repository.findAll().size(),1);


    }*/
}
