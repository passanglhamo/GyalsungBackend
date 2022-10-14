package com.microservice.erp.repositories;

import com.microservice.erp.domain.entities.UserInfo;
import com.microservice.erp.domain.models.Gender;
import com.microservice.erp.domain.repositories.IUserInfoRepository;
import com.microservice.erp.webapp.config.TestJPAH2Config;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestJPAH2Config.class})
@Transactional
@TestPropertySource(locations = {"classpath:h2-db.properties"})
public class IUserInfoRepositoryUnitTest {

    @Autowired
    IUserInfoRepository repository;

    @Test
    public void insert(){
        UserInfo userInfo = new UserInfo("Rajib The Coder", Gender.MALE, 24);
        repository.save(userInfo);

        List<UserInfo> list = repository.findByName("Rajib The Coder");
        Assert.assertTrue(Objects.nonNull(list));

        if (list != null && list.size() > 0){
            UserInfo userInfo2 = list.get(0);
            Assert.assertTrue(Objects.equals(userInfo.getId(), userInfo2.getId()));
        }
    }

    @Test
    public void update(){
        //TODO
    }

    @Test
    public void delete(){
        //TODO
    }

    @Test
    public void count(){
        //
        UserInfo userInfo = new UserInfo("Rajib The Coder", Gender.MALE, 24);
        repository.save(userInfo);

        long count = repository.count();
        Assert.assertTrue(count == 1);
    }

    @Test
    public void fetch(){
        //
        repository.save(new UserInfo("Sayed The Coder", Gender.MALE, 24));
        repository.save(new UserInfo("Evan The Pankha Coder", Gender.MALE, 24));
        repository.save(new UserInfo("Razib The Pagla", Gender.MALE, 26));
        //
        Page<UserInfo> paged = repository.findAll(PageRequest.of(0
                , 10
                , Sort.by(Sort.Order.asc("name"))));
        paged.get().forEach(userInfo -> System.out.println(userInfo.getId()));
    }
}