package com.alexIT.VioletsNeils.commands.adminCommands.CanselOrTransferRecordCommands.canselCommands;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.exception.EntityNotFoundException;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.factory.ConfirmKeyboardFactory;
import com.alexIT.VioletsNeils.service.TimeSlotService;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class CanselTimeCommand implements Command {

    private final UserSessionManager sessionManager;
    private final ConfirmKeyboardFactory confirmKeyboardFactory;
    private final TimeSlotService timeSlotService;
    private static final String INFO_ABOUT_CANSEL_RECORD = """
            Вы действительно хотите удалить запись ?
            Дата %s %s %s
            Время %s
            """;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && text.startsWith("/canselTime_")
                && state.equals(UserState.PREPARED)
                && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        String[] textArr = userDto.getText().split("_");
        Long timeSlotId = Long.parseLong(textArr[1]);
        TimeSlot timeSlot = timeSlotService.findById(timeSlotId).orElseThrow(() -> new EntityNotFoundException("Такой записи не существует!"));
        userSession.setSelectedRecordId(timeSlotId);
        userSession.setSelectedTime(timeSlot.getTime());
        KeyboardBuilder keyboardBuilder = confirmKeyboardFactory.create("/confirmCanselRecord", "/menu");
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
        return String.format(INFO_ABOUT_CANSEL_RECORD,
                userSession.getSelectedDate().getDayOfMonth(),
                MonthsAndDaysUtils.monthGenitiveForms.get(monthName),
                userSession.getSelectedDate().getYear(),
                userSession.getSelectedTime());
    }
}
