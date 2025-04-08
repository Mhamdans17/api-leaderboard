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
        log.info("🏆 [LEADERBOARD] Memulai proses leaderboard | Liga ID: {}", idLiga);

        log.debug("🔍 [LEADERBOARD] Mencari tim untuk liga ID: {}", idLiga);
        List<Team> teams = teamRepository.findByLigaIdLiga(idLiga);

        if (teams.isEmpty()) {
            log.warn("⚠️ [LEADERBOARD] Tidak ada tim ditemukan | Liga ID: {}", idLiga);
            throw new NotFoundException("No teams found for Liga ID: " + idLiga);
        } else {
            log.info("✅ [LEADERBOARD] Ditemukan {} tim | Liga ID: {}", teams.size(), idLiga);
        }

        // Log semua tim sebelum sorting
        log.info("📋 [LEADERBOARD] Daftar Tim Sebelum Sorting:");
        teams.forEach(team ->
                log.info("   - {}: {} poin (M:{} S:{} K:{})",
                        team.getNamaTeam(),
                        team.getPoin(),
                        team.getJumlahMenang(),
                        team.getJumlahImbang(),
                        team.getJumlahKalah())
        );

        log.debug("📊 [LEADERBOARD] Memulai sorting berdasarkan poin");
        List<Team> sortedTeams = teams.stream()
                .sorted(Comparator.comparingInt(Team::getPoin).reversed())
                .collect(Collectors.toList());

        // Log semua tim setelah sorting
        log.info("🏅 [LEADERBOARD] Hasil Leaderboard:");
        for (int i = 0; i < sortedTeams.size(); i++) {
            Team team = sortedTeams.get(i);
            log.info("{}. {} - {} poin (M:{} S:{} K:{})",
                    i + 1,
                    team.getNamaTeam(),
                    team.getPoin(),
                    team.getJumlahMenang(),
                    team.getJumlahImbang(),
                    team.getJumlahKalah());
        }

        // Log summary
        if (!sortedTeams.isEmpty()) {
            Team topTeam = sortedTeams.get(0);
            log.info("🎯 [LEADERBOARD] Summary | Liga ID: {} | Jumlah Tim: {} | Top Team: {} ({} poin)",
                    idLiga,
                    sortedTeams.size(),
                    topTeam.getNamaTeam(),
                    topTeam.getPoin());
        }

        return sortedTeams;
    }
}
