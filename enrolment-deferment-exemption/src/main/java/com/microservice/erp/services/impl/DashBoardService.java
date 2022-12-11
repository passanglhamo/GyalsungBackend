package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dao.DashBoardDao;
import com.microservice.erp.domain.dto.DashboardDto;
import com.microservice.erp.services.iServices.IDashBoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DashBoardService implements IDashBoardService {
    private final DashBoardDao dashBoardDao;

    public DashBoardService(DashBoardDao dashBoardDao) {
        this.dashBoardDao = dashBoardDao;
    }

    @Override
    public ResponseEntity<?> getRegistrationInfoFigures(String year) {
        DashboardDto dashboardDto = dashBoardDao.getRegistrationInfoFigures(year);
        return ResponseEntity.ok(dashboardDto);
    }
}
