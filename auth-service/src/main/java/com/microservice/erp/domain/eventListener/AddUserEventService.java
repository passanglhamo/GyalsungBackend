package com.microservice.erp.domain.eventListener;

import com.google.gson.Gson;
import com.microservice.erp.domain.entities.Role;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.repositories.RoleRepository;
import com.microservice.erp.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AddUserEventService {
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @KafkaListener(topics = {"${topic.addUser}"}, concurrency = "1")
    public void sendEmail(@Payload String message, Acknowledgment ack) throws Exception {

        Gson gson = new Gson();
        EventBusUser userEventInfo = gson.fromJson(message, EventBusUser.class);

        User userInfo = new User();
        Optional<User> userDb = repository.findByUserId(userEventInfo.getUserId());

        if (!userDb.isPresent()) {
            userInfo.setUserId(userEventInfo.getUserId());
            userInfo.setUsername(Objects.isNull(userEventInfo.getCid())?userEventInfo.getEmail():
                    userEventInfo.getCid());
            userInfo.setEmail(userEventInfo.getEmail());
            userInfo.setCid(userEventInfo.getCid());
            userInfo.setPassword(encoder.encode(userEventInfo.getPassword()));
            userInfo.setSecrets(User.createRandomMapOfSecret());
            Set<Role> saRoles = new HashSet<>();
            if (userEventInfo.getIsOpenUser().equals('Y')) {
//                Optional<Role> roleDb = roleRepository.findRoleByRoleName(userRole);
//                if (roleDb.isPresent()){
//                    role = roleDb.get();
//                }else {
//                    role.setRoleName(userRole);
//                }
//                user.addRoles(role);


                Role saRoleDb = roleRepository.findByIsOpenUser('Y');// to get student user role information
                saRoles.add(saRoleDb);
                userInfo.setRoles(saRoles);
            } else {
                userEventInfo.getRoles().forEach(roleId -> {
                    Role saRoleDb = roleRepository.findById(roleId).get();
                    saRoles.add(saRoleDb);
                });
                userInfo.setRoles(saRoles);
            }
            repository.save(userInfo);

        } else {
            repository.findById(userEventInfo.userId).ifPresent(user -> {
                user.setUserId(userEventInfo.userId);
                user.setUsername(userEventInfo.getCid());
                user.setEmail(userEventInfo.getEmail());
                user.setCid(userEventInfo.getCid());
                user.setPassword(encoder.encode(userEventInfo.getPassword()));
                user.setSecrets(User.createRandomMapOfSecret());
                Set<Role> saRoles = new HashSet<>();
                if (userEventInfo.getIsOpenUser().equals('Y')) {
                    Role saRoleDb = roleRepository.findByIsOpenUser('Y');// to get student user role information
                    saRoles.add(saRoleDb);
                    user.setRoles(saRoles);
                } else {
                    userEventInfo.getRoles().forEach(roleId -> {
                        Role saRoleDb = roleRepository.findById(roleId).get();
                        saRoles.add(saRoleDb);
                    });
                    userInfo.setRoles(saRoles);
                }
                repository.save(user);
            });
        }


    }

    @KafkaListener(topics = {"${topic.changeEmail}"}, concurrency = "1")
    public void changeEmail(@Payload String message, Acknowledgment ack) throws Exception {
        Gson gson = new Gson();
        EventBusUser userEventInfo = gson.fromJson(message, EventBusUser.class);
        Optional<User> userDb = repository.findByUserId(userEventInfo.userId);
        User userInfo = new ModelMapper().map(userDb, User.class);
        userInfo.setEmail(userEventInfo.email);
        repository.save(userInfo);
    }

    @KafkaListener(topics = {"${topic.changeUsername}"}, concurrency = "1")
    public void changeUsername(@Payload String message, Acknowledgment ack) throws Exception {
        Gson gson = new Gson();
        EventBusUser userEventInfo = gson.fromJson(message, EventBusUser.class);
        Optional<User> userDb = repository.findByUserId(userEventInfo.userId);
        User userInfo = new ModelMapper().map(userDb, User.class);
        userInfo.setUsername(userEventInfo.username);
        repository.save(userInfo);
    }


}
