//package com.microservice.erp.services.impl;
//
//import com.microservice.erp.domain.entities.UserInfo;
//import com.microservice.erp.domain.repository.IUserInfoRepository;
//import com.microservice.erp.services.iServices.IUserInfoService;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
///**
// * @author Rajib Kumer Ghosh
// *
// */
//
//@Service
//public class UserInfoService implements IUserInfoService {
//
//    private IUserInfoRepository repository;
//
//    public UserInfoService(IUserInfoRepository repository) {
//        this.repository = repository;
//    }
//
//    @Override
//    public void setIUserInfoRepository(IUserInfoRepository repository) {
//        this.repository = repository;
//    }
//
//    @Override
//    public UserInfo add(UserInfo aUserInfo) {
//        return repository.save(aUserInfo);
//    }
//
//    @Override
//    public UserInfo update(UserInfo aUserInfo) {
//        return repository.save(aUserInfo);
//    }
//
//    @Override
//    public boolean remove(Integer userid) {
//        if (repository.existsById(userid)){
//            repository.deleteById(userid);
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public Long totalCount() {
//        return repository.count();
//    }
//
//    @Override
//    public UserInfo findByUserID(Integer userId) {
//        Optional<UserInfo> isFound = repository.findById(userId);
//        if (isFound.isPresent()) return isFound.get();
//        else return null;
//    }
//
//    @Override
//    public List<UserInfo> findAllByUserID(List<Integer> userId) {
//        return repository.findAllById(userId);
//    }
//
//    @Override
//    public List<UserInfo> findAll(Integer page, Integer size) {
//        return repository.findAll(PageRequest.of(page, size)).getContent();
//    }
//}
