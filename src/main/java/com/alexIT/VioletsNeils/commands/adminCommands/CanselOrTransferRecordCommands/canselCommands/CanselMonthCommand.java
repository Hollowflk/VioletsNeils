package com.alexIT.VioletsNeils.commands.adminCommands.CanselOrTransferRecordCommands.canselCommands;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.factory.TransferMonthKeyboardFactory;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.factory.TransferRecordDateSelectionKeyboardFactory;
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
public class CanselMonthCommand implements Command {

    private final TransferRecordDateSelectionKeyboardFactory transferRecordDateSelectionKeyboardFactory;
    private final TransferMonthKeyboardFactory transferMonthKeyboardFactory;
    private final DailyRecordServiceImpl dailyRecordService;
    private final UserSessionManager sessionManager;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && (text.equals("/canselCurrentMonthAdmin") || text.equals("/canselNextMonthAdmin") || text.equals("/chooseCanselMonth"))
        && state.equals(UserState.PREPARED)
        && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        if (userDto.getText().equals("/chooseCanselMonth")) {
            return chooseMonth(userDto);
        }
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        LocalDate month;
        if (userDto.getText().equals("/canselCurrentMonthAdmin")) {
            month = LocalDate.now();
            userSession.setSelectedMonth(month);
        } else {
            month = LocalDate.now().plusMonths(1);
            userSession.setSelectedMonth(month);
        }
        KeyboardBuilder keyboardBuilder = transferRecordDateSelectionKeyboardFactory.create(dailyRecordService, month,
                "/canselDayCommand_%s", "/chooseCanselMonth");
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите день для удаления записи.")
                .replyMarkup(keyboard)
                .build();
    }

    private EditMessageText chooseMonth(TgUserDto dto) {
        KeyboardBuilder keyboardBuilder = transferMonthKeyboardFactory.create("cansel","/canselOrTransferRecord");
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(dto.getChatId())
                .messageId(dto.getMessageId())
                .text("Выберите месяц для удаления записи.")
                .replyMarkup(keyboard)
                .build();
    }
}
