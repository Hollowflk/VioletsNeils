package com.alexIT.VioletsNeils.session;

import com.alexIT.VioletsNeils.entity.Service;
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
    private LocalDate selectedDate;
    private LocalTime selectedTime;
    private String phoneNumber;
    private String fullName;

    public UserSession(Long userId) {
        state = UserState.NEW_USER;
        this.userId = userId;
    }
}
