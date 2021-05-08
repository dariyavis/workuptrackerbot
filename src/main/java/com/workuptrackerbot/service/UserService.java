package com.workuptrackerbot.service;

import com.workuptrackerbot.entity.UserEntity;
import com.workuptrackerbot.entity.UserProject;
import com.workuptrackerbot.repository.UPRepository;
import com.workuptrackerbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UPRepository upRepository;

    public boolean isUserExist(User user) {
        return userRepository.findByTlgId(user.getId()) != null;
    }

    public UserEntity createOrUpdateUser(User user) {
        UserEntity userEntity = getOrCreateUserByUserName(user.getUserName());
        userEntity.setTlgId(user.getId());
        userEntity.setRegistrDate(new Date());

        return userRepository.save(userEntity);
    }

    public UserEntity getUser(Integer id) {
        Optional<UserEntity> optional = Optional.ofNullable(userRepository.findByTlgId(id));
        return optional.isEmpty() ? null : optional.get();
    }

    public List<String> getUserNamesByProjectId(String project_id) {
        List<UserProject> ups = upRepository.findByProjectId(Long.valueOf(project_id));
        return ups.stream()
                .map(userProject -> userProject.getUserEntity().getUsername())
                .collect(Collectors.toList());
    }

    public UserEntity getOrCreateUserByUserName(String userName) {
        UserEntity userEntity = userRepository.findByUsername(userName);

        if (userEntity == null) {
            userEntity = new UserEntity();
            userEntity.setUsername(userName);
        }
        return userRepository.save(userEntity);
    }

    public boolean isRegistedUserByUserName(String userName) {
        return userRepository.existsByUsernameAndRegistrDateIsNotNull(userName);
    }
}
