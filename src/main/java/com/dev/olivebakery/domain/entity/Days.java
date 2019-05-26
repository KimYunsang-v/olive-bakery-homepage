package com.dev.olivebakery.domain.entity;

import com.dev.olivebakery.domain.enums.DayType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Days {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "days_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private DayType dayType;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "bread_id")
    private Bread bread;
}
