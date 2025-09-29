package com.alexIT.VioletsNeils.commands.adminCommands.CanselOrTransferRecordCommands;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.TransferRecordDateSelectionKeyboard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SelectMonthlyTransferCommand implements Command {

    private LocalDate selectedMonth;
    private final TransferRecordDateSelectionKeyboard transferRecordDateSelectionKeyboard;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        if (text != null && text.equals("/transferRecordCurrentMonthAdmin") && state.equals(UserState.PREPARED) && roleUser.equals(RoleUser.ADMIN)) {
            selectedMonth = LocalDate.now();
            return true;
        }
        if (text != null && text.equals("/transferRecordNextMonthAdmin") && state.equals(UserState.PREPARED) && roleUser.equals(RoleUser.ADMIN)) {
            selectedMonth = LocalDate.now().plusMonths(1);
            return true;
        }
        return false;
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        transferRecordDateSelectionKeyboard.setSelectedMonth(selectedMonth);
        InlineKeyboardMarkup keyboard = transferRecordDateSelectionKeyboard.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите день.")
                .replyMarkup(keyboard)
                .build();
    }
}
