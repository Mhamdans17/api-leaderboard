package com.imtokyodev.apiliga.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Entity
public class Liga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLiga;

    @Column(nullable = false)
    private String namaLiga;

    @Column(nullable = false)
    private int jumlahTeam;

    @Column(nullable = false)
    private String deskripsiLiga;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date createAt;
}
