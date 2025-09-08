package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.TgUser;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;

public class SignUpCommand implements Command{
    @Override
    public boolean supports(String text) {
        return text != null && text.equalsIgnoreCase("/signUp");
    }

    @Override
    public BotApiMethod<?> handler(TgUser tgUser) {
        return null;
    }
}
