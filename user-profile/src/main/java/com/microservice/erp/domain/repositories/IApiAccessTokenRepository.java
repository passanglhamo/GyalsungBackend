package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.ApiAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IApiAccessTokenRepository extends JpaRepository<ApiAccessToken, String> {
    ApiAccessToken findTop1ByOrderByIdDesc();
}
