package com.alexIT.VioletsNeils.service.impl;

import com.alexIT.VioletsNeils.entity.DailyRecord;
import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.service.TimeSlotService;
import com.alexIT.VioletsNeils.session.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final DailyRecordServiceImpl dailyRecordService;
    private final TimeSlotService timeSlotService;

    public boolean transferRecord(UserSession userSession) {
        Optional<DailyRecord> optionalDailyRecord = dailyRecordService.findByDate(userSession.getSelectedDate());
        DailyRecord dailyRecord;
        dailyRecord = optionalDailyRecord.orElseGet(() -> dailyRecordService.save(new DailyRecord(userSession.getSelectedDate())));
        Optional<TimeSlot> optionalTimeSlot = timeSlotService.findById(userSession.getSelectedRecordId());
        if (optionalTimeSlot.isPresent()) {
            TimeSlot selectedTimeSlot = optionalTimeSlot.get();
            timeSlotService.deleteById(userSession.getSelectedRecordId());
            TimeSlot transferSlot = new TimeSlot(
                    userSession.getSelectedTime(),
                    dailyRecord,
                    selectedTimeSlot.getService(),
                    selectedTimeSlot.getUser()
            );
            dailyRecord.getTimeSlotList().add(transferSlot);
            dailyRecordService.save(dailyRecord);
            return true;
        }
        return false;
    }
}
