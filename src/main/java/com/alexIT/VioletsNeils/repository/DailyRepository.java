package com.alexIT.VioletsNeils.repository;

import com.alexIT.VioletsNeils.entity.DailyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyRepository extends JpaRepository<DailyRecord, Long> {
}
