package com.alexIT.VioletsNeils.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class NeilBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient client;

    public NeilBot(TelegramClient client) {
        this.client = client;
    }

    @Override
    public void consume(Update update) {

    }
}
