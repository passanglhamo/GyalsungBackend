package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, BigInteger> {
    Screen findTop1ByOrderByScreenIdDesc();

    List<Screen> findAllByOrderByScreenNameAsc();

    Optional<Screen> findByScreenName(String screenName);

    Screen findByScreenId(Integer screenId);

}
