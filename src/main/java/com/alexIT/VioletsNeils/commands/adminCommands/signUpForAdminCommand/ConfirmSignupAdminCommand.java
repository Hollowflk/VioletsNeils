package com.alexIT.VioletsNeils.commands.adminCommands.signUpForAdminCommand;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;

@Component
@RequiredArgsConstructor
public class ConfirmSignupAdminCommand implements Command {

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && text.equals("/confirmSignUpAdmin")
                && state.equals(UserState.PREPARED)
                && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        return null;
    }
}
