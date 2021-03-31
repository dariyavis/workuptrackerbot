package com.workuptrackerbot.service;

import com.workuptrackerbot.bottools.springbottools.commands.ActionState;
import com.workuptrackerbot.entity.ActionStateEntity;
import com.workuptrackerbot.entity.UserEntity;
import com.workuptrackerbot.repository.CommandStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class ActionStateService {

    @Autowired
    private CommandStateRepository commandStateRepository;

    @Autowired
    private UserService userService;

    public ActionState getActionState(User user) {
        ActionStateEntity entity = commandStateRepository.findByUserEntityId(user.getId());
        return new ActionState(user, entity.getAction(), entity.getData());
    }

    public void saveActionState(ActionState actionState) {
        ActionStateEntity entity = commandStateRepository.findByUserEntityId(actionState.getUser().getId());
        if(entity == null) {
            entity = new ActionStateEntity();
            UserEntity userEntity = userService.getUser(actionState.getUser().getId());
            entity.setUserEntity(userEntity);
        }
        entity.setAction(actionState.getAction());
        entity.setData(actionState.getData());
        commandStateRepository.save(entity);
    }
}
