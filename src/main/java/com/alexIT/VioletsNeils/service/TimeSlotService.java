package com.alexIT.VioletsNeils.service;

import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeSlotService {

    private final TimeSlotRepository repository;

    public List<TimeSlot> findAll() {
        return repository.findAll();
    }

    public List<TimeSlot> findAllByUserId(Long userId) {
        return repository.findAllByUser_UserId(userId);
    }
}
