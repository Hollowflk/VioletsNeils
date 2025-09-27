package com.alexIT.VioletsNeils.commands.adminCommands.CanselOrTransferRecordCommands;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.UserState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;

@Component
public class TransferRecordCommand implements Command {
    @Override
    public boolean supports(String text, UserState state) {
        return text != null && text.equals("/transferRecord_admin") && state.equals(UserState.PREPARED);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        return null;
    }
}
