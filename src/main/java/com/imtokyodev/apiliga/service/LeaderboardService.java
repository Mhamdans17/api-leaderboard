package com.imtokyodev.apiliga.service;

import com.imtokyodev.apiliga.entity.Team;
import com.imtokyodev.apiliga.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {

    private final TeamRepository teamRepository;
    public LeaderboardService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> getLeaderboard(Long idLiga) {
        List<Team> teams = teamRepository.findByLigaIdLiga(idLiga);

        return teams.stream()
                .sorted(Comparator.comparingInt(Team::getPoin).reversed())
                .collect(Collectors.toList());
    }
}
