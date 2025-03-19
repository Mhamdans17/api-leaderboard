package com.imtokyodev.apiliga.repository;

import com.imtokyodev.apiliga.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByLigaIdLiga(Long ligaId);
    int countByLigaIdLiga(Long idLiga);
}
