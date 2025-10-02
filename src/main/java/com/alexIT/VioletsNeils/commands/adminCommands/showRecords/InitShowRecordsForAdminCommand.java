package com.alexIT.VioletsNeils.commands.adminCommands.showRecords;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.factory.TransferMonthKeyboardFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class InitShowRecordsForAdminCommand implements Command {

    private final TransferMonthKeyboardFactory transferMonthKeyboardFactory;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && text.equals("/showRecordsForAdmin")
                && state.equals(UserState.PREPARED)
                && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        KeyboardBuilder keyboardBuilder = transferMonthKeyboardFactory.create("showRecords", "/menu");
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите месяц для просмотра записей.")
                .replyMarkup(keyboard)
                .build();
    }
}
