package com.alexIT.VioletsNeils.commands.adminCommands.CanselOrTransferRecordCommands.canselCommands;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.factory.TransferRecordDateSelectionKeyboardFactory;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.factory.TransferRecordSelectionKeyboardFactory;
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
public class CanselDayCommand implements Command {

    private final TransferRecordSelectionKeyboardFactory transferRecordSelectionKeyboardFactory;
    private final TransferRecordDateSelectionKeyboardFactory transferRecordDateSelectionKeyboardFactory;
    private final DailyRecordServiceImpl recordService;
    private final UserSessionManager sessionManager;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && (text.startsWith("/canselDayCommand_") || text.startsWith("/chooseCanselDay"))
                && state.equals(UserState.PREPARED)
                && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        if (userDto.getText().equals("/chooseCanselDay")) {
            return chooseDay(userDto, userSession.getSelectedMonth());
        }
        String[] textArr = userDto.getText().split("_");
        String[] splitTextArr = textArr[1].split("-");
        LocalDate selectedDate = LocalDate.of(
                Integer.parseInt(splitTextArr[0]),
                Integer.parseInt(splitTextArr[1]),
                Integer.parseInt(splitTextArr[2])
        );
        userSession.setSelectedDate(selectedDate);
        KeyboardBuilder keyboardBuilder = transferRecordSelectionKeyboardFactory.create(
                recordService.findByDate(selectedDate).orElseThrow().getTimeSlotList(),
                "/canselTime_%s", "/chooseCanselDay"
        );
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите запись для удаления.")
                .replyMarkup(keyboard)
                .build();
    }

    private EditMessageText chooseDay(TgUserDto dto, LocalDate month) {
        KeyboardBuilder keyboardBuilder = transferRecordDateSelectionKeyboardFactory.create(recordService, month,
                "/canselDayCommand_%s", "/chooseCanselMonth");
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(dto.getChatId())
                .messageId(dto.getMessageId())
                .text("Выберите день для удаления записи.")
                .replyMarkup(keyboard)
                .build();
    }
}
