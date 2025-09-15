package com.alexIT.VioletsNeils.session;

import com.alexIT.VioletsNeils.entity.Service;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class UserSession {

    private final Long userId;
    private Service selectedService;
    private LocalDate selectedDate;
    private LocalTime selectedTime;

    public UserSession(Long userId) {
        this.userId = userId;
    }

    public boolean isComplete() {
        return selectedService != null && selectedDate != null && selectedTime != null;
    }
}
