package com.alexIT.VioletsNeils;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.commands.MenuCommand;
import com.alexIT.VioletsNeils.commands.UnknowCommand;
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
        }
        return "Неизвестная команда!";
    }

    // TODO: Убрать это отсюда
    private TgUser createUser(Update update) {
        return new TgUser(
                update.getMessage().getChatId(),
                RoleUser.USER
        );
    }
}
