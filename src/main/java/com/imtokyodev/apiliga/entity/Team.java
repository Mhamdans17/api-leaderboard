package com.imtokyodev.apiliga.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTeam;

    @Column(nullable = false)
    private String namaTeam;

    @ManyToOne
    @JoinColumn(name = "id_liga", nullable = false)
    private Liga liga;

    private int poin = 0; // Poin tim, default 0
    private int jumlahMenang = 0; // Jumlah menang, default 0
    private int jumlahKalah = 0; // Jumlah kalah, default 0

    @Column(nullable = false)
    private String deskripsiLiga;
}
