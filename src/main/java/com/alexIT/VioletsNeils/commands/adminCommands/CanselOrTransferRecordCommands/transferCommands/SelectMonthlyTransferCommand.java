package com.alexIT.VioletsNeils.commands.adminCommands.CanselOrTransferRecordCommands.transferCommands;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.factory.TransferRecordDateSelectionKeyboardFactory;
import com.alexIT.VioletsNeils.service.impl.DailyRecordServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SelectMonthlyTransferCommand implements Command {

    private final TransferRecordDateSelectionKeyboardFactory keyboardFactory;
    private final DailyRecordServiceImpl dailyRecordService;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && (text.equals("/transferRecordCurrentMonthAdmin") || text.equals("/transferRecordNextMonthAdmin")) && state.equals(UserState.PREPARED) && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        LocalDate selectedMonth;
        if (userDto.getText().equals("/transferRecordCurrentMonthAdmin")) {
            selectedMonth = LocalDate.now();
        } else {
            selectedMonth = LocalDate.now().plusMonths(1);
        }
        KeyboardBuilder keyboardBuilder = keyboardFactory.create(dailyRecordService, selectedMonth, "/transferRecordDate_%s", "/transferRecord_admin");
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите день.")
                .replyMarkup(keyboard)
                .build();
    }
}
