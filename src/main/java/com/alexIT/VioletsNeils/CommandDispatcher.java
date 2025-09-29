package com.alexIT.VioletsNeils;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.commands.userCommands.UnknowCommand;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.entity.TgUser;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.service.UserRoleService;
import com.alexIT.VioletsNeils.service.impl.UserServiceImpl;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandDispatcher {

    private final List<Command> commandList;
    private final UnknowCommand unknowCommand;
    private final UserServiceImpl userService;
    private final UserSessionManager sessionManager;
    private final UserRoleService userRoleService;

    public BotApiMethod<?> handler(Update update) {
        TgUserDto userDto = userService.createUserDto(update);
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        Optional<TgUser> optionalTgUser = userService.findById(userSession.getUserId());
        if (optionalTgUser.isPresent() && sessionManager.isUserPrepared(userDto.getUserId())) {
            userSession.setState(UserState.PREPARED);
            userSession.setPhoneNumber(optionalTgUser.get().getPhoneNumber());
            userSession.setFullName(optionalTgUser.get().getFullName());
            userSession.setRoleUser(userRoleService.getRole(userSession.getUserId()));
        }
        String textCommand = getText(update);
        log.info("Получена команда {}", textCommand);
        Command command = commandList.stream()
                .filter(cmd -> cmd.supports(textCommand, userSession.getState(), userSession.getRoleUser()))
                .findFirst()
                .orElse(unknowCommand);
        log.info("Выполняется команда {}", command.getClass().getName());
        return command.handler(userDto);
    }

    private String getText(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return update.getMessage().getText();
        } else {
            return update.getCallbackQuery().getData();
        }
    }
}
