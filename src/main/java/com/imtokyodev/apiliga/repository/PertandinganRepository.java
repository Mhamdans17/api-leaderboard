package com.imtokyodev.apiliga.repository;

import com.imtokyodev.apiliga.entity.Pertandingan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PertandinganRepository extends JpaRepository<Pertandingan, Long> {
    List<Pertandingan> findByLigaIdLiga(Long idLiga);

    // Hitung jumlah pertandingan antara dua tim (home dan away)
    @Query("SELECT COUNT(p) FROM Pertandingan p " +
            "WHERE (p.teamHome.idTeam = :idTeamHome AND p.teamAway.idTeam = :idTeamAway) " +
            "OR (p.teamHome.idTeam = :idTeamAway AND p.teamAway.idTeam = :idTeamHome)")
    int countPertandinganBetweenTeams(@Param("idTeamHome") Long idTeamHome, @Param("idTeamAway") Long idTeamAway);
}
