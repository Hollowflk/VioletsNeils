package com.alexIT.VioletsNeils.commands.adminCommands.CanselOrTransferRecordCommands.transferCommands;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.factory.ConfirmKeyboardFactory;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class SelectedTransferTimeCommand implements Command {

    private final UserSessionManager sessionManager;
    private final ConfirmKeyboardFactory confirmKeyboardFactory;
    private static final String INFO_ABOUT_TRANSFER = """
            Перенести запись на %s %s 
            на время %s
            Подтвердить ?
            """;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && text.startsWith("/transferTime_")
                && state.equals(UserState.PREPARED)
                && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        String[] textArr = userDto.getText().split("_");
        LocalTime selectedTime = LocalTime.of(
                Integer.parseInt(textArr[1]),
                0
        );
        userSession.setSelectedTime(selectedTime);
        KeyboardBuilder keyboardBuilder = confirmKeyboardFactory.create("/confirmTransfer", "/menu");
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text(createMessage(userSession))
                .replyMarkup(keyboard)
                .build();
    }

    private String createMessage(UserSession userSession) {
        String monthName = MonthsAndDaysUtils.getNameMonth(userSession.getSelectedDate().getMonth().getValue());
        return String.format(INFO_ABOUT_TRANSFER,
                userSession.getSelectedDate().getDayOfMonth(),
                MonthsAndDaysUtils.monthGenitiveForms.get(monthName),
                userSession.getSelectedTime());
    }
}
