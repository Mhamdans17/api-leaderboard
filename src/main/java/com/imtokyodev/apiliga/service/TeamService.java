package com.imtokyodev.apiliga.service;

import com.imtokyodev.apiliga.entity.Liga;
import com.imtokyodev.apiliga.entity.Team;
import com.imtokyodev.apiliga.exception.BadRequestException;
import com.imtokyodev.apiliga.exception.NotFoundException;
import com.imtokyodev.apiliga.model.TeamRequest;
import com.imtokyodev.apiliga.model.TeamResponse;
import com.imtokyodev.apiliga.repository.LigaRepository;
import com.imtokyodev.apiliga.repository.TeamRepository;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final LigaRepository ligaRepository;

    public TeamService(TeamRepository teamRepository, LigaRepository ligaRepository) {
        this.teamRepository = teamRepository;
        this.ligaRepository = ligaRepository;
    }

    public TeamResponse addTeam(TeamRequest teamRequest) {
        Liga liga = ligaRepository.findById(teamRequest.getIdLiga())
                .orElseThrow(() -> new NotFoundException("Liga tidak ditemukan"));

        int jumlahTeam = teamRepository.countByLigaIdLiga(liga.getIdLiga());
        if (jumlahTeam >= liga.getJumlahTeam()) {
            throw new BadRequestException("Jumlah tim sudah mencapai batas maksimal");
        }

        // Request Add Team
        Team team = new Team();
        team.setNamaTeam(teamRequest.getNamaTeam());
        team.setLiga(liga);
        team.setDeskripsiLiga(liga.getDeskripsiLiga());

        teamRepository.save(team);

        // Response Add Team
        TeamResponse teamResponse = new TeamResponse();
        teamResponse.setIdTeam(team.getIdTeam());
        teamResponse.setNamaTeam(team.getNamaTeam());
        teamResponse.setIdLiga(team.getLiga().getIdLiga());
        teamResponse.setNamaLiga(team.getLiga().getNamaLiga());
        teamResponse.setMessage("Tim berhasil ditambah");

        return teamResponse;
    }

}
