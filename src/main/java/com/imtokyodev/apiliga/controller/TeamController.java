package com.imtokyodev.apiliga.controller;

import com.imtokyodev.apiliga.model.TeamRequest;
import com.imtokyodev.apiliga.model.TeamResponse;
import com.imtokyodev.apiliga.service.TeamService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class TeamController {

    private final TeamService teamService;
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/add/team")
    public ResponseEntity<?> createTeam(@Valid @RequestBody TeamRequest request) {
        TeamResponse response = teamService.addTeam(request);
        return ResponseEntity.ok(response);
    }
}
