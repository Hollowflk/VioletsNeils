package com.alexIT.VioletsNeils.dto;

import com.alexIT.VioletsNeils.enums.RoleUser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TgUserDto {

    private Long userId;
    private Long chatId;
    private Integer messageId;
    private RoleUser role;
}
