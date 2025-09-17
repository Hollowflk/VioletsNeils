package com.alexIT.VioletsNeils.bot;

import com.alexIT.VioletsNeils.CommandDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
public class NeilBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient client;
    private final CommandDispatcher commandDispatcher;

    public NeilBot(TelegramClient client, CommandDispatcher commandDispatcher) {
        this.client = client;
        this.commandDispatcher = commandDispatcher;
    }

    @Override
    public void consume(Update update) {
        BotApiMethod<?> message = commandDispatcher.handler(update);
        try {
            if (update.hasCallbackQuery()) {
                client.execute(message);
                client.execute(new AnswerCallbackQuery(update.getCallbackQuery().getId()));
            } else {
                client.execute(message);
            }
        } catch (TelegramApiException e) {
            log.debug(e.getMessage());
        }
    }
}
