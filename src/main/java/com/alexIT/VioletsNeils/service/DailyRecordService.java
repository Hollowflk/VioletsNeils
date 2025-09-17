package com.alexIT.VioletsNeils.service;

import com.alexIT.VioletsNeils.entity.DailyRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyRecordService {

    DailyRecord findById(Long id);
    List<DailyRecord> findAll();
    DailyRecord save(DailyRecord dailyRecord);
    DailyRecord update(DailyRecord updatedRecord);
    void delete(Long id);
    List<DailyRecord> findAllByDateBetween(LocalDate start, LocalDate end);
    Optional<DailyRecord> findByDate(LocalDate date);
}
