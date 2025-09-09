package com.alexIT.VioletsNeils.entity;

import com.alexIT.VioletsNeils.enums.RoleUser;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TgUser {

    private Long userId;
    private Long chatId;
    private Integer messageId;
    private String fullName;
    private String phoneNumber;
    private LocalDateTime dateTime;
    private RoleUser role;

    public TgUser(Long chatId, Integer messageId, RoleUser role) {
        this.chatId = chatId;
        this.messageId = messageId;
        this.role = role;
    }
}
