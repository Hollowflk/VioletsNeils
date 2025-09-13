package com.alexIT.VioletsNeils.dto;

import com.alexIT.VioletsNeils.entity.Service;
import com.alexIT.VioletsNeils.enums.RoleUser;

import lombok.Data;

@Data
public class TgUserDto {

    private Long userId;
    private Long chatId;
    private Integer messageId;
    private RoleUser role;
    private Service service;

    public TgUserDto(Long userId, Long chatId, Integer messageId, RoleUser role) {
        this.userId = userId;
        this.chatId = chatId;
        this.messageId = messageId;
        this.role = role;
    }
}
