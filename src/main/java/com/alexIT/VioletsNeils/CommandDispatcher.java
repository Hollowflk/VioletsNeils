package com.alexIT.VioletsNeils;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.commands.UnknowCommand;
import com.alexIT.VioletsNeils.convector.Convector;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.service.impl.TgUserServiceImpl;
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
    private final TgUserServiceImpl userService;

    public CommandDispatcher(List<Command> commandList, UnknowCommand unknowCommand, TgUserServiceImpl userService, Convector convector) {
        this.commandList = commandList;
        this.unknowCommand = unknowCommand;
        this.userService = userService;
    }

    public BotApiMethod<?> handler(Update update) {
        TgUserDto userDto = userService.createUserDto(update);
        Command command = commandList.stream()
                .filter(cmd -> cmd.supports(getText(update)))
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
