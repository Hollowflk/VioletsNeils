package com.alexIT.VioletsNeils.commands.adminCommands.signUpForAdminCommand;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.service.impl.ConfirmService;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import lombok.RequiredArgsConstructor;
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
public class ConfirmSignupAdminCommand implements Command {

    private static final String MSG = """
            Вы записали пользователя %s
            На услугу: %s
            Дата и время записи: %s %s на %s ч
            """;

    private static final String MESSAGE_ABOUT_NOTIFICATION = """
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
        return text != null && text.equals("/confirmSignUpAdmin")
                && state.equals(UserState.PREPARED)
                && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        SendMessage msg = createMessageAboutNotification(userSession);
        confirmService.confirmRecordForAdmin(userSession, msg);
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text(createMsg(userSession))
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboard(List.of(
                                new InlineKeyboardRow(
                                        InlineKeyboardButton.builder()
                                                .text("Меню")
                                                .callbackData("/menu")
                                                .build())
                        ))
                        .build())
                .build();
    }

    private String createMsg(UserSession userSession) {
        String monthName = MonthsAndDaysUtils.getNameMonth(userSession.getSelectedDate().getMonth().getValue());
        return String.format(
                MSG,
                userSession.getSelectedUser().getFullName(),
                userSession.getSelectedService().getName(),
                userSession.getSelectedDate().getDayOfMonth(),
                MonthsAndDaysUtils.monthGenitiveForms.get(monthName),
                userSession.getSelectedTime()
        );
    }

    private SendMessage createMessageAboutNotification(UserSession userSession) {
        String monthName = MonthsAndDaysUtils.getNameMonth(userSession.getSelectedDate().getMonth().getValue());
        String message = String.format(
                MESSAGE_ABOUT_NOTIFICATION,
                userSession.getSelectedUser().getFullName(),
                userSession.getSelectedService().getName(),
                userSession.getSelectedDate().getDayOfMonth(),
                MonthsAndDaysUtils.monthGenitiveForms.get(monthName),
                userSession.getSelectedTime()
        );
        return SendMessage.builder()
                .chatId(userSession.getSelectedUser().getUserId())
                .text(message)
                .build();
    }
}
