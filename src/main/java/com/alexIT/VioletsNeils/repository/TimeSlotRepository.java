package com.alexIT.VioletsNeils.repository;

import com.alexIT.VioletsNeils.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    List<TimeSlot> findAllByUser_UserId(Long userId);
}