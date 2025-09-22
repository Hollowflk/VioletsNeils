package com.alexIT.VioletsNeils.service;

import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TimeSlotService {

    private final TimeSlotRepository repository;
    public final Map<LocalTime, String> timeMap = new LinkedHashMap<>();

    public TimeSlotService(TimeSlotRepository repository) {
        this.repository = repository;
        timeMap.put(LocalTime.of(10, 0), "10:00");
        timeMap.put(LocalTime.of(12, 0), "12:00");
        timeMap.put(LocalTime.of(15, 0), "15:00");
        timeMap.put(LocalTime.of(17, 0), "17:00");
    }

    public List<TimeSlot> findAll() {
        return repository.findAll();
    }

    public List<TimeSlot> findAllByUserId(Long userId) {
        return repository.findAllByUser_UserId(userId);
    }
}
