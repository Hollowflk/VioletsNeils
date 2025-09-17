package com.alexIT.VioletsNeils.service.impl;

import com.alexIT.VioletsNeils.entity.DailyRecord;
import com.alexIT.VioletsNeils.exception.EntityNotFoundException;
import com.alexIT.VioletsNeils.repository.DailyRepository;
import com.alexIT.VioletsNeils.service.DailyRecordService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DailyRecordServiceImpl implements DailyRecordService {

    private final DailyRepository repository;

    public DailyRecordServiceImpl(DailyRepository repository) {
        this.repository = repository;
    }

    @Override
    public DailyRecord findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Заданная дата не найдена!"));
    }

    @Override
    public List<DailyRecord> findAll() {
        return repository.findAll();
    }

    @Override
    public DailyRecord save(DailyRecord dailyRecord) {
        return repository.save(dailyRecord);
    }

    @Override
    public DailyRecord update(DailyRecord updatedRecord) {
        DailyRecord dailyRecord = new DailyRecord();
        dailyRecord.setDate(updatedRecord.getDate());
        dailyRecord.setTimeSlotList(updatedRecord.getTimeSlotList());
        return repository.save(dailyRecord);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<DailyRecord> findAllByDateBetween(LocalDate start, LocalDate end) {
        return repository.findAllByDateBetween(start, end);
    }

    @Override
    public Optional<DailyRecord> findByDate(LocalDate date) {
        return repository.findByDate(date);
    }
}
