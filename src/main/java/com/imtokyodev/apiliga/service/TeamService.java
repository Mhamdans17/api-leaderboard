package com.imtokyodev.apiliga.service;

import com.imtokyodev.apiliga.entity.Liga;
import com.imtokyodev.apiliga.entity.Team;
import com.imtokyodev.apiliga.exception.BadRequestException;
import com.imtokyodev.apiliga.exception.NotFoundException;
import com.imtokyodev.apiliga.model.TeamRequest;
import com.imtokyodev.apiliga.model.TeamResponse;
import com.imtokyodev.apiliga.repository.LigaRepository;
import com.imtokyodev.apiliga.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final LigaRepository ligaRepository;

    public TeamService(TeamRepository teamRepository, LigaRepository ligaRepository) {
        this.teamRepository = teamRepository;
        this.ligaRepository = ligaRepository;
    }

    public TeamResponse addTeam(TeamRequest teamRequest) {
        // 1. Log awal proses
        log.info("🏁 [ADD TEAM] Memulai proses penambahan tim | Request: {}", teamRequest);

        // 2. Cari liga dengan logging
        log.debug("🔎 [ADD TEAM] Mencari liga dengan ID: {}", teamRequest.getIdLiga());
        Liga liga = ligaRepository.findById(teamRequest.getIdLiga())
                .orElseThrow(() -> {
                    log.error("❌ [ADD TEAM] Liga tidak ditemukan | ID Liga: {}", teamRequest.getIdLiga());
                    return new NotFoundException("Liga tidak ditemukan");
                });
        log.info("✅ [ADD TEAM] Liga ditemukan | ID: {} | Nama: {}", liga.getIdLiga(), liga.getNamaLiga());

        // 3. Validasi jumlah tim
        log.debug("🧮 [ADD TEAM] Menghitung jumlah tim yang terdaftar");
        int jumlahTeam = teamRepository.countByLigaIdLiga(liga.getIdLiga());
        log.debug("📊 [ADD TEAM] Jumlah tim: {}/{}", jumlahTeam, liga.getJumlahTeam());

        if (jumlahTeam >= liga.getJumlahTeam()) {
            log.warn("⛔ [ADD TEAM] Gagal - Kuota tim penuh | Liga ID: {} | {}/{}",
                    liga.getIdLiga(),
                    jumlahTeam,
                    liga.getJumlahTeam());
            throw new BadRequestException("Jumlah tim sudah mencapai batas maksimal");
        }

        // 4. Proses pembuatan tim
        log.debug("🛠️ [ADD TEAM] Membuat entitas tim baru");
        Team team = new Team();
        team.setNamaTeam(teamRequest.getNamaTeam());
        team.setLiga(liga);
        team.setDeskripsiLiga(liga.getDeskripsiLiga());

        // 5. Simpan tim
        log.debug("💾 [ADD TEAM] Menyimpan tim ke database");
        Team savedTeam = teamRepository.save(team);
        log.info("🟢 [ADD TEAM] Tim berhasil disimpan | ID Tim: {}", savedTeam.getIdTeam());

        // 6. Membuat response
        log.debug("📦 [ADD TEAM] Membuat response");
        TeamResponse teamResponse = new TeamResponse();
        teamResponse.setIdTeam(savedTeam.getIdTeam());
        teamResponse.setNamaTeam(savedTeam.getNamaTeam());
        teamResponse.setIdLiga(savedTeam.getLiga().getIdLiga());
        teamResponse.setNamaLiga(savedTeam.getLiga().getNamaLiga());
        teamResponse.setMessage("Tim berhasil ditambah");

        log.info("🎉 [ADD TEAM] Proses selesai | Response: {}", teamResponse);
        return teamResponse;
    }

}
