package com.alexIT.VioletsNeils.convector;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.entity.TgUser;
import com.alexIT.VioletsNeils.enums.RoleUser;
import org.springframework.stereotype.Component;

@Component
public class ConvectorUser {

    public TgUser convertDtoInUser(TgUserDto dto) {
        return new TgUser(
                dto.getUserId(),
                "noName",
                "noPhone",
                RoleUser.USER
        );
    }
}
