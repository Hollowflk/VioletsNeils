package com.alexIT.VioletsNeils.commands.adminCommands.showRecords;

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
public class SelectMonthToShowRecordsCommand implements Command {

    private final TransferRecordDateSelectionKeyboardFactory transferRecordDateSelectionKeyboardFactory;
    private final DailyRecordServiceImpl dailyRecordService;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && (text.equals("/showRecordsCurrentMonthAdmin") || text.equals("/showRecordsNextMonthAdmin"))
                && state.equals(UserState.PREPARED)
                && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        LocalDate month;
        if (userDto.getText().equals("/showRecordsCurrentMonthAdmin")) {
            month = LocalDate.now();
        } else {
            month = LocalDate.now().plusMonths(1);
        }
        KeyboardBuilder keyboardBuilder = transferRecordDateSelectionKeyboardFactory.create(dailyRecordService, month,
                "/showRecordForAdmin_%s", "/showRecordsForAdmin");
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите день для просмотра записей.")
                .replyMarkup(keyboard)
                .build();
    }
}