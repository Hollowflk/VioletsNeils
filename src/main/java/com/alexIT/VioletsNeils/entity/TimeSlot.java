package com.alexIT.VioletsNeils.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@Entity(name = "time_slot")
public class TimeSlot implements Comparable<TimeSlot>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time")
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "daily_id")
    private DailyRecord dailyRecord;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private TgUser user;

    public TimeSlot(LocalTime time, DailyRecord dailyRecord, Service service, TgUser user) {
        this.time = time;
        this.dailyRecord = dailyRecord;
        this.service = service;
        this.user = user;
    }

    @Override
    public int compareTo(@NotNull TimeSlot o) {
        return dailyRecord.getDate().compareTo(o.getDailyRecord().getDate());
    }
}
