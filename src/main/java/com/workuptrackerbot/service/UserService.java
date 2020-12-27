package com.workuptrackerbot.service;

import com.workuptrackerbot.entity.UserEntity;
import com.workuptrackerbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.User;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

//    public UserService(UserRepository userRepository){
//        this.userRepository = userRepository;
//    }

//    public void createUser(String username, String firstName){
//        userRepository.save(user);
//    }

//    public void createUser(User user) {
//        com.workuptrackerbot.entity.User userDB = new com.workuptrackerbot.entity.User();
//        userDB.setId(user.getId());
//        userDB.setUsername(user.getUserName());
//        userDB.setFirstName(user.getFirstName());
//        userDB.setLastName(user.getLastName());
//        userDB.setFirstName(new Date());
//        userRepository.save(
//                new com.workuptrackerbot.entity.User(user.getId(),user.getUserName(), new Date()));
//    }

    public UserEntity getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean isUserExist(User user) {
        UserEntity userEntity = userRepository.findByUsername(user.getUserName());
        if(userEntity != null) {
            return true;
        }

        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setId(user.getId());
        newUserEntity.setUsername(user.getUserName());
        newUserEntity.setRegistrDate(new Date());
        //todo если ошибка создания юзера, бросить ошибку
        userRepository.save(newUserEntity);
        return false;
    }
}
