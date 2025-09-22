package com.alexIT.VioletsNeils;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.commands.UnknowCommand;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.service.impl.UserServiceImpl;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandDispatcher {

    private final List<Command> commandList;
    private final UnknowCommand unknowCommand;
    private final UserServiceImpl userService;
    private final UserSessionManager sessionManager;

    public BotApiMethod<?> handler(Update update) {
        TgUserDto userDto = userService.createUserDto(update);
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        if (userService.findById(userSession.getUserId()).isPresent()) {
            userSession.setState(UserState.PREPARED);
        }
        String textCommand = getText(update);
        log.info("Получена команда {}", textCommand);
        Command command = commandList.stream()
                .filter(cmd -> cmd.supports(textCommand, userSession.getState()))
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
