package com.alexIT.VioletsNeils.convector;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.entity.TgUser;
import org.springframework.stereotype.Component;

@Component
public class Convector {

    public TgUser convertUserToDtoUser(TgUserDto dto) {
        return new TgUser(
                dto.getUserId(),
                dto.getChatId(),
                dto.getMessageId(),
                dto.getRole()
        );
    }

    public TgUserDto convertDtoUserToUser(TgUser tgUser) {
        return new TgUserDto(
                tgUser.getUserId(),
                tgUser.getChatId(),
                tgUser.getMessageId(),
                tgUser.getRole()
        );
    }
}
