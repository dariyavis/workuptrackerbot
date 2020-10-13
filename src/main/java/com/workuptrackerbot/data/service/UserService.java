package com.workuptrackerbot.data.service;

import com.workuptrackerbot.data.repository.UserRepository;
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

    public void createUser(User user) {
//        com.workuptrackerbot.data.entity.User userDB = new com.workuptrackerbot.data.entity.User();
//        userDB.setId(user.getId());
//        userDB.setUsername(user.getUserName());
//        userDB.setFirstName(user.getFirstName());
//        userDB.setLastName(user.getLastName());
//        userDB.setFirstName(new Date());
        userRepository.save(
                new com.workuptrackerbot.data.entity.User(user.getId(),user.getUserName(), new Date()));
    }
}
