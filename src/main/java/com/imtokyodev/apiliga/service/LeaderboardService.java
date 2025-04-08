package com.imtokyodev.apiliga.service;

import com.imtokyodev.apiliga.entity.Team;
import com.imtokyodev.apiliga.exception.NotFoundException;
import com.imtokyodev.apiliga.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LeaderboardService {

    private final TeamRepository teamRepository;
    public LeaderboardService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> getLeaderboard(Long idLiga) {
        log.info("Id Liga: {}", idLiga);
        List<Team> teams = teamRepository.findByLigaIdLiga(idLiga);

        // Jika tidak ada tim yang ditemukan, lempar NotFoundException
        if (teams.isEmpty()) {
            log.warn("No teams found for Liga ID: {}", idLiga);
            throw new NotFoundException("No teams found for Liga ID: " + idLiga);
        } else {
            // Menyortir tim berdasarkan poin (dari yang tertinggi ke terendah)
            log.info("Sorting {} teams based on points", teams.size());
        }

        // Menyortir tim berdasarkan poin
        List<Team> sortedTeams = teams.stream()
                .sorted(Comparator.comparingInt(Team::getPoin).reversed())
                .collect(Collectors.toList());

        // Menambahkan log response dengan informasi tentang leaderboard
        log.info("Leaderboard generated for Liga ID {}: {} teams, top team: {} with {} points",
                idLiga,
                sortedTeams.size(),
                sortedTeams.isEmpty() ? "N/A" : sortedTeams.get(0).getNamaTeam(),
                sortedTeams.isEmpty() ? "N/A" : sortedTeams.get(0).getPoin());

        return sortedTeams;
    }
}
