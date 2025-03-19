package com.imtokyodev.apiliga.controller;

import com.imtokyodev.apiliga.entity.Team;
import com.imtokyodev.apiliga.service.LeaderboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LeaderboardController {

   private final LeaderboardService leaderboardService;
   public LeaderboardController(LeaderboardService leaderboardService) {
       this.leaderboardService = leaderboardService;
   }

    @GetMapping("/leaderboard/{idLiga}")
    public ResponseEntity<?> getLeaderboard(@PathVariable Long idLiga) {
        List<Team> leaderboard = leaderboardService.getLeaderboard(idLiga);
        return ResponseEntity.ok(leaderboard);
    }
}
