package com.alexIT.VioletsNeils.repository;

import com.alexIT.VioletsNeils.entity.DailyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyRepository extends JpaRepository<DailyRecord, Long> {

    List<DailyRecord> findAllByDateBetween(LocalDate start, LocalDate end);
}
