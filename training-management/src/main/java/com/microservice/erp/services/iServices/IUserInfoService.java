//package com.microservice.erp.services.iServices;
//
//import com.microservice.erp.domain.entities.UserInfo;
//import com.microservice.erp.domain.repository.IUserInfoRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
///**
// * @author Rajib Kumer Ghosh
// *
// */
//
//public interface IUserInfoService {
//    void setIUserInfoRepository(@Autowired IUserInfoRepository repository);
//    UserInfo add(UserInfo aUserInfo);
//    UserInfo update(UserInfo aUserInfo);
//    boolean remove(Integer userid);
//    Long totalCount();
//    UserInfo findByUserID(Integer userid);
//    List<UserInfo> findAllByUserID(List<Integer> userid);
//    List<UserInfo> findAll(Integer page, Integer size);
//}
