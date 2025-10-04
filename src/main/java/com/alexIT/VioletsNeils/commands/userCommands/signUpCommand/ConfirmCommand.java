package com.alexIT.VioletsNeils.commands.userCommands.signUpCommand;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.service.impl.ConfirmService;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConfirmCommand implements Command {

    private static final String MSG = """
            %s, Вы записались на процедуру %s
            на %s %s в %s
            Мастер Виолетта Вертий
            
            Местоположение: Гостиница «Тихая сосна», 2 этаж, кабинет 206
            Телефон для связи: +7 (951) 769-53-94
            
            До встречи🌸
            """;

    private final UserSessionManager sessionManager;
    private final ConfirmService confirmService;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && text.equals("/confirm") && state.equals(UserState.COMPLETED) && roleUser.equals(RoleUser.USER);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        confirmService.confirmRecordForUser(userDto);
        String monthName = MonthsAndDaysUtils.getNameMonth(userSession.getSelectedDate().getMonth().getValue());
        String message = String.format(
                MSG,
                userSession.getFullName(),
                userSession.getSelectedService().getName(),
                userSession.getSelectedDate().getDayOfMonth(),
                monthName,
                userSession.getSelectedTime()
        );
        return SendMessage.builder()
                .chatId(userSession.getUserId())
                .text(message)
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboard(
                                List.of(
                                        new InlineKeyboardRow(InlineKeyboardButton.builder()
                                                .text("Меню")
                                                .callbackData("/menu")
                                                .build())
                                )
                        ).build()
                )
                .build();
    }


}
