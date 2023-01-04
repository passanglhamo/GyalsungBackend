/*
package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.SaUser;
import com.microservice.erp.domain.repositories.IUserInfoRepository;
import com.microservice.erp.services.iServices.IUserInfoService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

*/
/**
 * @author Rajib Kumer Ghosh
 *
 *//*


@Service
public class UserInfoService implements IUserInfoService {

    private IUserInfoRepository repository;

    public UserInfoService(IUserInfoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void setIUserInfoRepository(IUserInfoRepository repository) {
        this.repository = repository;
    }

    @Override
    public SaUser add(SaUser aSaUser) {
        return repository.save(aSaUser);
    }

    @Override
    public SaUser update(SaUser aSaUser) {
        return repository.save(aSaUser);
    }

    @Override
    public boolean remove(Long userid) {
//        if (repository.existsById(userid)){
//            repository.deleteById(userid);
//            return true;
//        }
        return false;
    }

    @Override
    public Long totalCount() {
        return repository.count();
    }

    @Override
    public SaUser findByUserID(Long userId) {
//        Optional<SaUser> isFound = repository.findById(userId);
        Optional<SaUser> isFound =null;
        if (isFound.isPresent()) return isFound.get();
        else return null;
    }

    @Override
    public List<SaUser> findAllByUserID(List<Long> userId) {
//        return repository.findAllById(userId);
        return null;
    }

    @Override
    public List<SaUser> findAll(Integer page, Integer size) {
        return repository.findAll(PageRequest.of(page, size)).getContent();
    }
}
*/
