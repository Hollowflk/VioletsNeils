package com.alexIT.VioletsNeils.commands.adminCommands.CanselOrTransferRecordCommands.transferCommands;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.service.impl.TransferService;
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
public class ConfirmTransfer implements Command {

    private final UserSessionManager sessionManager;
    private final TransferService transferService;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && text.startsWith("/confirmTransfer") && state.equals(UserState.PREPARED) && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        String text;
        if (transferService.transferRecord(userSession)) {
            text = "Запись перенесена!";
        } else {
            text = "Запись не перенесена!";
        }
        return SendMessage.builder()
                .chatId(userDto.getChatId())
                .text(text)
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboard(List.of(
                                new InlineKeyboardRow(
                                        InlineKeyboardButton.builder()
                                                .text("Меню")
                                                .callbackData("/menu")
                                                .build()
                                )
                        ))
                        .build()
                )
                .build();
    }
}
