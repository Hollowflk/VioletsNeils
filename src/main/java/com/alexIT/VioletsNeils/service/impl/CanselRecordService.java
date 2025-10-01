package com.alexIT.VioletsNeils.service.impl;

import com.alexIT.VioletsNeils.service.TimeSlotService;
import com.alexIT.VioletsNeils.session.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CanselRecordService {

    private final TimeSlotService timeSlotService;

    public boolean canselRecord(UserSession userSession) {
        return timeSlotService.deleteById(userSession.getSelectedRecordId());
    }
}
