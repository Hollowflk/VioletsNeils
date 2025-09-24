package com.alexIT.VioletsNeils.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "daily_record")
@NoArgsConstructor
public class DailyRecord implements Comparable<DailyRecord>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @OneToMany(mappedBy = "dailyRecord", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<TimeSlot> timeSlotList = new ArrayList<>();

    public DailyRecord(LocalDate date) {
        this.date = date;
    }

    @Override
    public int compareTo(@NotNull DailyRecord o) {
        return date.compareTo(o.getDate());
    }
}
