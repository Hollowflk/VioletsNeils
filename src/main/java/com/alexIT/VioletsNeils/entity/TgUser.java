package com.alexIT.VioletsNeils.entity;

import com.alexIT.VioletsNeils.enums.RoleUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Entity
@NoArgsConstructor
@Table(name = "TgUser")
public class TgUser {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleUser role;

    public TgUser(Long userId,  RoleUser role) {
        this.userId = userId;
        this.role = role;
    }
}
