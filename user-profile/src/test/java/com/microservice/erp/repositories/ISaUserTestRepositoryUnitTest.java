package com.microservice.erp.repositories;

import com.microservice.erp.domain.entities.SaUser;
import com.microservice.erp.domain.models.Gender;
import com.microservice.erp.domain.repositories.ISaUserRepository;
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

/**
 * @author Rajib Kumer Ghosh
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestJPAH2Config.class})
@Transactional
@TestPropertySource(locations = {"classpath:h2-db.properties"})
public class ISaUserTestRepositoryUnitTest {

    @Autowired
    ISaUserRepository repository;

    @Test
    public void insert(){
        SaUser saUser = new SaUser();
        repository.save(saUser);

        /*List<SaUser> list = repository.findByName("Rajib The Coder");
        Assert.assertTrue(Objects.nonNull(list));

        if (list != null && list.size() > 0){
            SaUser userInfo2 = list.get(0);
            Assert.assertTrue(Objects.equals(saUser.getId(), userInfo2.getId()));
        }*/
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
        SaUser saUser = new SaUser();
        repository.save(saUser);

        long count = repository.count();
        Assert.assertTrue(count == 1);
    }

    @Test
    public void fetch(){
        //
//        repository.save(new SaUser("Sayed The Coder"));
//        repository.save(new SaUser("Evan The Pankha Coder", Gender.MALE, 24));
        repository.save(new SaUser());
        //
        Page<SaUser> paged = repository.findAll(PageRequest.of(0
                , 10
                , Sort.by(Sort.Order.asc("name"))));
        paged.get().forEach(userInfo -> System.out.println(userInfo.getId()));
    }
}