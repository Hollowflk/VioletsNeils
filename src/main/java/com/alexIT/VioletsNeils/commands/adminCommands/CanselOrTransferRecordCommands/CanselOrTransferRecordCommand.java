package com.alexIT.VioletsNeils.commands.adminCommands.CanselOrTransferRecordCommands;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.ChooseCanselOrTransferRecordKeyboard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class CanselOrTransferRecordCommand implements Command {

    private final ChooseCanselOrTransferRecordKeyboard chooseCanselOrTransferRecordKeyboard;

    @Override
    public boolean supports(String text, UserState state) {
        return text != null && text.equals("/canselOrTransferRecord") && state.equals(UserState.PREPARED);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        InlineKeyboardMarkup keyboard = chooseCanselOrTransferRecordKeyboard.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите действие")
                .replyMarkup(keyboard)
                .build();
    }
}
