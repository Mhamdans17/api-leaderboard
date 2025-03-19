package com.imtokyodev.apiliga.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Pertandingan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPertandingan;

    @ManyToOne
    @JoinColumn(name = "id_team_home", nullable = false)
    private Team teamHome;

    @ManyToOne
    @JoinColumn(name = "id_team_away", nullable = false)
    private Team teamAway;

    private int skorHome;
    private int skorAway;

    @ManyToOne
    @JoinColumn(name = "id_liga", nullable = false)
    private Liga liga;
}
