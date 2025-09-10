package com.alexIT.VioletsNeils.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity(name = "time_slot")
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "daily_id")
    private DailyRecord dailyRecord;

    @Column(name = "time")
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private TgUser user;
}
