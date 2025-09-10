package com.alexIT.VioletsNeils;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.commands.UnknowCommand;
import com.alexIT.VioletsNeils.convector.Convector;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@Component
public class CommandDispatcher {

    private final List<Command> commandList;
    private final UnknowCommand unknowCommand;
    private final UserServiceImpl userServiceImpl;

    public CommandDispatcher(List<Command> commandList, UnknowCommand unknowCommand, UserServiceImpl userService) {
        this.commandList = commandList;
        this.unknowCommand = unknowCommand;
        this.userServiceImpl = userService;
    }

    public BotApiMethod<?> handler(Update update) {
        TgUserDto userDto = userServiceImpl.createUserDto(update);
        String textCommand = getText(update);
        log.info("Получена команда {}", textCommand);
        Command command = commandList.stream()
                .filter(cmd -> cmd.supports(textCommand))
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
