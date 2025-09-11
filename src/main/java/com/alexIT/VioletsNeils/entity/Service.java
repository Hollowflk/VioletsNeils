package com.alexIT.VioletsNeils.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;

@Data
@Entity
@Table(name = "service")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "duration")
    private Duration duration;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ServiceCategory category;
}
