package com.alexIT.VioletsNeils;

import com.alexIT.VioletsNeils.commands.*;
import com.alexIT.VioletsNeils.entity.TgUser;
import com.alexIT.VioletsNeils.enums.RoleUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CommandDispatcher {

    private final List<Command> commandList = new ArrayList<>();
    private final UnknowCommand unknowCommand = new UnknowCommand();

    public CommandDispatcher() {
        commandList.add(new MenuCommand());
        commandList.add(new SignUpCommand());
        commandList.add(new CurrentMonthCommand());
        commandList.add(new NextMonthCommand());
    }

    public BotApiMethod<?> handler(Update update) {
        Command command = commandList.stream()
                .filter(cmd -> cmd.supports(getText(update)))
                .findFirst()
                .orElse(unknowCommand);
        log.info("Выполняется команда {}", command.getClass().getName());
        return command.handler(createUser(update));
    }

    private String getText(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return update.getMessage().getText();
        } else {
            return update.getCallbackQuery().getData();
        }
    }

    // TODO: Убрать это отсюда
    private TgUser createUser(Update update) {
        if (update.hasCallbackQuery()) {
            return new TgUser(
                    update.getCallbackQuery().getMessage().getChatId(),
                    update.getCallbackQuery().getMessage().getMessageId(),
                    RoleUser.USER
            );
        }
        return new TgUser(
                update.getMessage().getChatId(),
                update.getMessage().getMessageId(),
                RoleUser.USER
        );
    }
}
