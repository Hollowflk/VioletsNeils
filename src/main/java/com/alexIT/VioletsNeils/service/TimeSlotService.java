package com.alexIT.VioletsNeils.service;

import com.alexIT.VioletsNeils.entity.DailyRecord;
import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.repository.TimeSlotRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TimeSlotService {

    private final TimeSlotRepository repository;
    public static final Map<LocalTime, String> TIME_MAP = new LinkedHashMap<>();

    public TimeSlotService(TimeSlotRepository repository) {
        this.repository = repository;
        TIME_MAP.put(LocalTime.of(10, 0), "10:00");
        TIME_MAP.put(LocalTime.of(12, 0), "12:00");
        TIME_MAP.put(LocalTime.of(15, 0), "15:00");
        TIME_MAP.put(LocalTime.of(17, 0), "17:00");
    }

    public Optional<TimeSlot> findById(Long id) {
        return repository.findById(id);
    }

    public List<TimeSlot> findAll() {
        return repository.findAll();
    }

    public List<TimeSlot> findAllByUserId(Long userId) {
        return repository.findAllByUser_UserId(userId);
    }

    @Transactional
    public boolean deleteById(Long timeSlotId) {
        Optional<TimeSlot> optionalTimeSlot = repository.findById(timeSlotId);
        if (optionalTimeSlot.isPresent()) {
            TimeSlot timeSlot = optionalTimeSlot.get();
            DailyRecord dailyRecord = timeSlot.getDailyRecord();

            List<TimeSlot> toRemove = dailyRecord.getTimeSlotList().stream()
                    .filter(ts -> ts.getService().getId().equals(timeSlot.getService().getId())
                            && ts.getUser().getUserId().equals(timeSlot.getUser().getUserId()))
                    .toList();

            for (TimeSlot ts : toRemove) {
                dailyRecord.getTimeSlotList().remove(ts);
                repository.delete(ts);
            }
            return true;
        }
        return false;
    }
}
