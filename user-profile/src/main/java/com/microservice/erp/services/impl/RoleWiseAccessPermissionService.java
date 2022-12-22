package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dao.RoleWiseAccessPermissionDao;
import com.microservice.erp.domain.dto.PermissionListDto;
import com.microservice.erp.services.iServices.IRoleWiseAccessPermissionService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;


@Service
public class RoleWiseAccessPermissionService implements IRoleWiseAccessPermissionService {
    private final RoleWiseAccessPermissionDao roleWiseAccessPermissionDao;

    //    private final RoleWiseAccessPermissionRepository roleWiseAccessPermissionRepository;
    public RoleWiseAccessPermissionService(RoleWiseAccessPermissionDao roleWiseAccessPermissionDao) {
        this.roleWiseAccessPermissionDao = roleWiseAccessPermissionDao;
    }


    public List<PermissionListDto> getScreens(BigInteger roleId) {
        List<PermissionListDto> permissionListDtos;
        boolean isRoleMapped = roleWiseAccessPermissionDao.getIsRoleMapped(roleId);
        if (isRoleMapped) {
            permissionListDtos = roleWiseAccessPermissionDao.getRoleMappedScreens(roleId);
        } else {
            permissionListDtos = roleWiseAccessPermissionDao.getRoleUnmappedScreens();
        }
        return permissionListDtos;
    }

    public List<PermissionListDto> getRoleMappedScreens(BigInteger roleId) {
        return roleWiseAccessPermissionDao.getRoleMappedScreens(roleId);
    }

    /*public ResponseMessage savePermission(PermissionDTO permissionDTO, CurrentUser currentUser) {
        ResponseMessage responseMessage = new ResponseMessage();
        RoleWiseAccessPermission permission = new RoleWiseAccessPermission();
        String permissionId;

        for (PermissionListDTO permissionListDTO : permissionDTO.getPermissionListDTOS()) {
            permissionId = UuidGenerator.generateUuid();
            permission.setScreenId(permissionListDTO.getScreenId());
            permission.setRoleId(permissionDTO.getRoleId());
            permission.setViewAllowed(permissionListDTO.getViewAllowed());
            permission.setSaveAllowed(permissionListDTO.getSaveAllowed());
            permission.setEditAllowed(permissionListDTO.getEditAllowed());
            permission.setDeleteAllowed(permissionListDTO.getDeleteAllowed());
            permission.setCreatedBy(currentUser.getUserId());
            permission.setCreatedDate(new Date());
            if (!permissionListDTO.getPermissionId().equals("")) {
                permission.setPermissionId(permissionListDTO.getPermissionId());
                roleWiseAccessPermissionRepository.save(permission);
            } else {
                permission.setPermissionId(permissionId);
                roleWiseAccessPermissionRepository.save(permission);
            }
        }
        responseMessage.setStatus(SystemDataInt.MESSAGE_STATUS_SUCCESSFUL.value());
        responseMessage.setText("Saved successfully.");
        return responseMessage;
    }*/
}
