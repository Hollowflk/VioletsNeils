package com.alexIT.VioletsNeils.commands.adminCommands.CanselOrTransferRecordCommands.canselCommands;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.service.impl.CanselRecordService;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConfirmCanselCommand implements Command {

    private final CanselRecordService canselRecordService;
    private final UserSessionManager sessionManager;


    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && text.equals("/confirmCanselRecord")
                && state.equals(UserState.PREPARED)
                && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        String text = canselRecordService.canselRecord(userSession) ? "Запись удаленна!" : "Запись не была удаленна!";
        return SendMessage.builder()
                .chatId(userDto.getChatId())
                .text(text)
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboard(List.of(
                                new InlineKeyboardRow(
                                        InlineKeyboardButton.builder()
                                                .text("Мею")
                                                .callbackData("/menu")
                                                .build())
                        ))
                        .build())
                .build();
    }
}
