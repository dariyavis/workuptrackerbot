package com.workuptrackerbot.service;

import com.workuptrackerbot.bottools.springbottools.commands.CommandState;
import com.workuptrackerbot.entity.CommandStateEntity;
import com.workuptrackerbot.entity.UserEntity;
import com.workuptrackerbot.repository.CommandStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class CommandStateService {

    @Autowired
    private CommandStateRepository commandStateRepository;

    @Autowired
    private UserService userService;

    public CommandState getCommandState(User user) {
        CommandStateEntity entity = commandStateRepository.findByUserEntityId(user.getId());
        return new CommandState(user, entity.getCommand(), entity.getIndex());
    }

    public void saveCommandState(CommandState commandState) {
        CommandStateEntity entity = commandStateRepository.findByUserEntityId(commandState.getUser().getId());
        if(entity == null) {
            entity = new CommandStateEntity();
            UserEntity userEntity = userService.getUser(commandState.getUser().getId());
            entity.setUserEntity(userEntity);
        }
        entity.setCommand(commandState.getCommand());
        entity.setIndex(commandState.getIndex());
        commandStateRepository.save(entity);
    }
}
