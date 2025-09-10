package com.alexIT.VioletsNeils.entity;

import com.alexIT.VioletsNeils.enums.RoleUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Entity
public class TgUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "message_id")
    private Integer messageId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleUser role;

    public TgUser(Long userId ,Long chatId, Integer messageId, RoleUser role) {
        this.userId = userId;
        this.chatId = chatId;
        this.messageId = messageId;
        this.role = role;
    }
}
