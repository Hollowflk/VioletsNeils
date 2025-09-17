package com.alexIT.VioletsNeils.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "daily_record")
@NoArgsConstructor
public class DailyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @OneToMany(mappedBy = "dailyRecord", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TimeSlot> timeSlotList = new ArrayList<>();

    public DailyRecord(LocalDate date) {
        this.date = date;
    }
}
