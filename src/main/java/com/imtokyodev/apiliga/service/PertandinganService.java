package com.imtokyodev.apiliga.service;

import com.imtokyodev.apiliga.entity.Liga;
import com.imtokyodev.apiliga.entity.Pertandingan;
import com.imtokyodev.apiliga.entity.Team;
import com.imtokyodev.apiliga.exception.BadRequestException;
import com.imtokyodev.apiliga.exception.NotFoundException;
import com.imtokyodev.apiliga.model.PertandinganRequest;
import com.imtokyodev.apiliga.model.PertandinganResponse;
import com.imtokyodev.apiliga.repository.LigaRepository;
import com.imtokyodev.apiliga.repository.PertandinganRepository;
import com.imtokyodev.apiliga.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PertandinganService {

    private final PertandinganRepository pertandinganRepository;
    private final TeamRepository teamRepository;
    private final LigaRepository ligaRepository;

    public PertandinganService(PertandinganRepository pertandinganRepository,
                               TeamRepository teamRepository,
                               LigaRepository ligaRepository) {
        this.pertandinganRepository = pertandinganRepository;
        this.teamRepository = teamRepository;
        this.ligaRepository = ligaRepository;
    }

    public PertandinganResponse getPertandingan(PertandinganRequest request) {
        log.info("Received request to create match between teamHomeId: {} and teamAwayId: {} in Liga ID: {}",
                request.getIdTeamHome(), request.getIdTeamAway(), request.getIdLiga());

        Team teamHome = teamRepository.findById(request.getIdTeamHome())
                .orElseThrow(() -> {
                    log.error("Team home with ID {} not found", request.getIdTeamHome());
                    return new NotFoundException("Tim home tidak ditemukan");
                });

        Team teamAway = teamRepository.findById(request.getIdTeamAway())
                .orElseThrow(() -> {
                    log.error("Team away with ID {} not found", request.getIdTeamAway());
                    return new NotFoundException("Tim away tidak ditemukan");
                });

        Liga liga = ligaRepository.findById(request.getIdLiga())
                .orElseThrow(() -> {
                    log.error("Liga with ID {} not found", request.getIdLiga());
                    return new NotFoundException("Liga tidak ditemukan");
                });

        if (!teamHome.getLiga().getIdLiga().equals(liga.getIdLiga())) {
            log.error("Team home with ID {} is not part of Liga ID {}", teamHome.getIdTeam(), liga.getIdLiga());
            throw new BadRequestException("Tim home tidak termasuk dalam liga ini");
        }

        if (!teamAway.getLiga().getIdLiga().equals(liga.getIdLiga())) {
            log.error("Team away with ID {} is not part of Liga ID {}", teamAway.getIdTeam(), liga.getIdLiga());
            throw new BadRequestException("Tim away tidak termasuk dalam liga ini");
        }

        // Cek apakah kedua tim sudah bertanding 2 kali
        int jumlahPertandingan = pertandinganRepository.countPertandinganBetweenTeams(
                teamHome.getIdTeam(), teamAway.getIdTeam());
        if (jumlahPertandingan >= 2) {
            log.error("Teams {} and {} have already played 2 times", teamHome.getNamaTeam(), teamAway.getNamaTeam());
            throw new BadRequestException("Tim " + teamHome.getNamaTeam() + " dan " + teamAway.getNamaTeam() +
                    " sudah bertanding 2 kali");
        }

        // Update poin, jumlah menang, dan jumlah kalah
        if (request.getSkorHome() > request.getSkorAway()) {
            // Tim home menang
            teamHome.setPoin(teamHome.getPoin() + 3);
            teamHome.setJumlahMenang(teamHome.getJumlahMenang() + 1);
            teamAway.setJumlahKalah(teamAway.getJumlahKalah() + 1);
            log.info("Team home {} wins, points updated. {} points", teamHome.getNamaTeam(), teamHome.getPoin());
        } else if (request.getSkorHome() < request.getSkorAway()) {
            // Tim away menang
            teamAway.setPoin(teamAway.getPoin() + 3);
            teamAway.setJumlahMenang(teamAway.getJumlahMenang() + 1);
            teamHome.setJumlahKalah(teamHome.getJumlahKalah() + 1);
            log.info("Team away {} wins, points updated. {} points", teamAway.getNamaTeam(), teamAway.getPoin());
        } else {
            // Seri
            teamHome.setPoin(teamHome.getPoin() + 1);
            teamAway.setPoin(teamAway.getPoin() + 1);
            log.info("The match between {} and {} is a draw", teamHome.getNamaTeam(), teamAway.getNamaTeam());
        }

        // Buat pertandingan baru
        Pertandingan pertandingan = new Pertandingan();
        pertandingan.setTeamHome(teamHome);
        pertandingan.setTeamAway(teamAway);
        pertandingan.setSkorHome(request.getSkorHome());
        pertandingan.setSkorAway(request.getSkorAway());
        pertandingan.setLiga(liga);

        pertandingan = pertandinganRepository.save(pertandingan);
        log.info("Match between {} and {} successfully saved with match ID: {}",
                teamHome.getNamaTeam(), teamAway.getNamaTeam(), pertandingan.getIdPertandingan());

        // Buat response
        PertandinganResponse response = new PertandinganResponse();
        response.setIdPertandingan(pertandingan.getIdPertandingan());
        response.setIdTeamHome(pertandingan.getTeamHome().getIdTeam());
        response.setNamaTeamHome(pertandingan.getTeamHome().getNamaTeam());
        response.setIdTeamAway(pertandingan.getTeamAway().getIdTeam());
        response.setNamaTeamAway(pertandingan.getTeamAway().getNamaTeam());
        response.setSkorHome(pertandingan.getSkorHome());
        response.setSkorAway(pertandingan.getSkorAway());
        response.setIdLiga(pertandingan.getLiga().getIdLiga());
        response.setMessage("Pertandingan berhasil dimulai");

        // Log response
        log.info("Response for match creation: {}", response);

        return response;
    }
}
