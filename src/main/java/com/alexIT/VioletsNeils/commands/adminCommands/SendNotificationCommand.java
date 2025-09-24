package com.alexIT.VioletsNeils.commands.adminCommands;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.service.impl.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SendNotificationCommand implements Command {

    private LocalDate date;
    private final NotificationService notificationService;

    @Override
    public boolean supports(String text, UserState state) {
        if (text != null && text.startsWith("/sendNotification_") && state.equals(UserState.PREPARED)) {
            String[] textArray = text.split("_");
            String selectedDate = textArray[1];
            String[] dateArray = selectedDate.split("-");
            date = LocalDate.of(
                    Integer.parseInt(dateArray[0]),
                    Integer.parseInt(dateArray[1]),
                    Integer.parseInt(dateArray[2])
            );
            return true;
        }
        return false;
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        String text = notificationService.sendNotification(date);
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text(text)
                .build();
    }
}
