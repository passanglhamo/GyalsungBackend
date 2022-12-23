package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dao.RoleWiseAccessPermissionDao;
import com.microservice.erp.domain.dto.PermissionDto;
import com.microservice.erp.domain.dto.PermissionListDto;
import com.microservice.erp.domain.entities.RoleWiseAccessPermission;
import com.microservice.erp.domain.repositories.RoleWiseAccessPermissionRepository;
import com.microservice.erp.services.iServices.IRoleWiseAccessPermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;


@Service
public class RoleWiseAccessPermissionService implements IRoleWiseAccessPermissionService {
    private final RoleWiseAccessPermissionDao roleWiseAccessPermissionDao;

    private final RoleWiseAccessPermissionRepository roleWiseAccessPermissionRepository;

    public RoleWiseAccessPermissionService(RoleWiseAccessPermissionDao roleWiseAccessPermissionDao
            , RoleWiseAccessPermissionRepository roleWiseAccessPermissionRepository) {
        this.roleWiseAccessPermissionDao = roleWiseAccessPermissionDao;
        this.roleWiseAccessPermissionRepository = roleWiseAccessPermissionRepository;
    }


    public ResponseEntity<?> getScreens(BigInteger roleId) {
        List<PermissionListDto> permissionListDtos;
        RoleWiseAccessPermission isRoleMapped = roleWiseAccessPermissionRepository.findTop1ByRoleId(roleId);
        if (isRoleMapped != null) {
            permissionListDtos = roleWiseAccessPermissionDao.getRoleMappedScreens(roleId);
        } else {
            permissionListDtos = roleWiseAccessPermissionDao.getRoleUnmappedScreens();
        }
        return ResponseEntity.ok(permissionListDtos);
    }

    public List<PermissionListDto> getRoleMappedScreens(BigInteger roleId) {
        return roleWiseAccessPermissionDao.getRoleMappedScreens(roleId);
    }

    public ResponseEntity<?> savePermission(PermissionDto permissionDto) {
        RoleWiseAccessPermission permission = new RoleWiseAccessPermission();

        for (PermissionListDto permissionListDto : permissionDto.getPermissionListDtos()) {
            permission.setScreenId(permissionListDto.getScreen_id());
            permission.setRoleId(permissionListDto.getRole_id());
            permission.setViewAllowed(permissionListDto.getView_allowed());
            permission.setSaveAllowed(permissionListDto.getSave_allowed());
            permission.setEditAllowed(permissionListDto.getEdit_allowed());
            permission.setDeleteAllowed(permissionListDto.getDelete_allowed());

            if (permissionListDto.getPermission_id() != null) {
                roleWiseAccessPermissionRepository.save(permission);
            }
//            else {
////                permission.setPermissionId(permissionId);
//                roleWiseAccessPermissionRepository.save(permission);
//            }
        }
        return ResponseEntity.ok("Data saved successfully.");
    }
}
