package com.alexIT.VioletsNeils;

import com.alexIT.VioletsNeils.roles.RoleUser;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TgUser {

    private Long userId;
    private Long chatId;
    private String fullName;
    private String phoneNumber;
    private LocalDateTime dateTime;
    private RoleUser role;

    public TgUser(Long chatId, RoleUser role) {
        this.chatId = chatId;
        this.role = role;
    }
}
