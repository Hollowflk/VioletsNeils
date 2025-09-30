package com.alexIT.VioletsNeils.commands.adminCommands.CanselOrTransferRecordCommands;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.TransferMonthKeyboardFactory;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.DaysKeyboardFactory;
import com.alexIT.VioletsNeils.service.impl.DailyRecordServiceImpl;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SelectedMonthTransferCommand implements Command {

    private final DaysKeyboardFactory daysKeyboardFactory;
    private final DailyRecordServiceImpl dailyRecordService;
    private final TransferMonthKeyboardFactory transferMonthKeyboardFactory;
    private final UserSessionManager sessionManager;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && (text.equals("/selectedTransferNextMonthAdmin") || text.equals("/selectedTransferCurrentMonthAdmin") || text.equals("/chooseTransferMonth"))
                && state.equals(UserState.PREPARED)
                && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        LocalDate selectedMonth;
        if (userDto.getText().equals("/selectedTransferCurrentMonthAdmin")) {
            selectedMonth = LocalDate.now();
            userSession.setSelectedMonth(selectedMonth);
        } else {
            selectedMonth = LocalDate.now().plusMonths(1);
            userSession.setSelectedMonth(selectedMonth);
        }

        if (userDto.getText().equals("/chooseTransferMonth")) {
            return chooseMonth(userDto);
        }
        KeyboardBuilder keyboardBuilder = daysKeyboardFactory.create(dailyRecordService, selectedMonth.getYear(), selectedMonth.getMonth(),
                "/transferDate_%d-%d-%d",
                "/chooseTransferMonth");
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите день для переноса.")
                .replyMarkup(keyboard)
                .build();
    }

    private EditMessageText chooseMonth(TgUserDto dto) {
        KeyboardBuilder keyboardBuilder = transferMonthKeyboardFactory.create("selectedTransfer", "/transferRecord_admin");
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(dto.getChatId())
                .messageId(dto.getMessageId())
                .text("Выберите месяц куда перенести запись.")
                .replyMarkup(keyboard)
                .build();
    }
}
