package com.workuptrackerbot.service;

import com.workuptrackerbot.entity.UserEntity;
import com.workuptrackerbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean isUserExist(User user) {
        return userRepository.existsById(user.getId());
    }

    public UserEntity createOrUpdateUser(User user) {
        Optional<UserEntity> opt = userRepository.findById(user.getId());
        UserEntity newUserEntity;

        if(opt.isEmpty()) {
            newUserEntity = new UserEntity();
            newUserEntity.setId(user.getId());
            newUserEntity.setRegistrDate(new Date());
        } else {
            newUserEntity = opt.orElseThrow();
        }
        newUserEntity.setUsername(user.getUserName());
        return userRepository.save(newUserEntity);
    }

    public UserEntity getUser(Integer id) {
        Optional<UserEntity> optional = userRepository.findById(id);
        if(optional.isEmpty()) return null;
        return userRepository.findById(id).orElseThrow();
    }
}
