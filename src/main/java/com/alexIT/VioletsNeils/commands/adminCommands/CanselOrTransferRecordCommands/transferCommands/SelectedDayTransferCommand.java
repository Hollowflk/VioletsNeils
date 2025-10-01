package com.alexIT.VioletsNeils.commands.adminCommands.CanselOrTransferRecordCommands.transferCommands;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.factory.DaysKeyboardFactory;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.factory.TimeKeyboardBuilderFactory;
import com.alexIT.VioletsNeils.service.TimeSlotService;
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
public class SelectedDayTransferCommand implements Command {

    private final TimeKeyboardBuilderFactory timeKeyboardBuilderFactory;
    private final DaysKeyboardFactory daysKeyboardFactory;
    private final DailyRecordServiceImpl dailyRecordService;
    private final TimeSlotService timeSlotService;
    private final UserSessionManager sessionManager;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && (text.startsWith("/transferDate_") || text.equals("/chooseTransferDay")) && state.equals(UserState.PREPARED) && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        if (userDto.getText().equals("/chooseTransferDay")) {
            return chooseDay(userDto, userSession.getSelectedMonth());
        }
        String[] textArr = userDto.getText().split("_");
        String[] dateArr = textArr[1].split("-");
        LocalDate selectedDate = LocalDate.of(
                Integer.parseInt(dateArr[0]),
                Integer.parseInt(dateArr[1]),
                Integer.parseInt(dateArr[2])
        );
        userSession.setSelectedDate(selectedDate);
        KeyboardBuilder keyboardBuilder = timeKeyboardBuilderFactory.create(dailyRecordService, timeSlotService, selectedDate,
                "/transferTime_%s",
                "/chooseTransferDay");
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите время куда нужно вставить запись")
                .replyMarkup(keyboard)
                .build();
    }

    private EditMessageText chooseDay(TgUserDto dto, LocalDate selectedDate) {
        KeyboardBuilder keyboardBuilder = daysKeyboardFactory.create(dailyRecordService, selectedDate.getYear(), selectedDate.getMonth(),
                "/transferDate_%d-%d-%d",
                "/chooseTransferMonth");
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(dto.getChatId())
                .messageId(dto.getMessageId())
                .text("Выберите день для переноса.")
                .replyMarkup(keyboard)
                .build();
    }
}
