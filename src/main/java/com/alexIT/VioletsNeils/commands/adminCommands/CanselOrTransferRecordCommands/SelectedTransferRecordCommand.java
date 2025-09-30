package com.alexIT.VioletsNeils.commands.adminCommands.CanselOrTransferRecordCommands;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.factory.TransferMonthKeyboardFactory;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class SelectedTransferRecordCommand implements Command {

    private final UserSessionManager sessionManager;
    private final TransferMonthKeyboardFactory factory;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && text.startsWith("/selectedTransferRecord_")
                && state.equals(UserState.PREPARED)
                && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        String[] textArr = userDto.getText().split("_");
        long selectedRecordId = Long.parseLong(textArr[1]);
        userSession.setSelectedRecordId(selectedRecordId);
        KeyboardBuilder keyboardBuilder = factory.create("selectedTransfer", "/transferRecord_admin");
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите месяц куда перенести запись.")
                .replyMarkup(keyboard)
                .build();
    }
}
