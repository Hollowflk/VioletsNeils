package com.alexIT.VioletsNeils.session;

import com.alexIT.VioletsNeils.entity.Service;
import com.alexIT.VioletsNeils.entity.TgUser;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class UserSession {

    private UserState state;
    private final Long userId;
    private Service selectedService;
    private String phoneNumber;
    private String fullName;
    private RoleUser roleUser;

    private Long selectedRecordId;
    private LocalDate selectedDate;
    private LocalTime selectedTime;
    private LocalDate selectedMonth;
    private TgUser selectedUser;

    public UserSession(Long userId) {
        state = UserState.NEW_USER;
        this.userId = userId;
        roleUser = RoleUser.USER;
    }
}
