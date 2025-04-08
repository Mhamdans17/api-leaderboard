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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
        log.info("🏟️ [PERTANDINGAN] Memulai proses pertandingan | Request: {}", request);

        // Validasi tim tidak boleh sama
        if (request.getIdTeamHome().equals(request.getIdTeamAway())) {
            log.warn("⚠️ [PERTANDINGAN] Tim home dan away sama | ID: {}", request.getIdTeamHome());
            throw new BadRequestException("Team home dan team away tidak boleh sama");
        }

        // Cari tim home
        log.debug("🔍 [PERTANDINGAN] Mencari tim home ID: {}", request.getIdTeamHome());
        Team teamHome = teamRepository.findById(request.getIdTeamHome())
                .orElseThrow(() -> {
                    log.error("❌ [PERTANDINGAN] Tim home tidak ditemukan | ID: {}", request.getIdTeamHome());
                    return new NotFoundException("Tim home tidak ditemukan");
                });

        // Cari tim away
        log.debug("🔍 [PERTANDINGAN] Mencari tim away ID: {}", request.getIdTeamAway());
        Team teamAway = teamRepository.findById(request.getIdTeamAway())
                .orElseThrow(() -> {
                    log.error("❌ [PERTANDINGAN] Tim away tidak ditemukan | ID: {}", request.getIdTeamAway());
                    return new NotFoundException("Tim away tidak ditemukan");
                });

        // Cari liga
        log.debug("🔍 [PERTANDINGAN] Mencari liga ID: {}", request.getIdLiga());
        Liga liga = ligaRepository.findById(request.getIdLiga())
                .orElseThrow(() -> {
                    log.error("❌ [PERTANDINGAN] Liga tidak ditemukan | ID: {}", request.getIdLiga());
                    return new NotFoundException("Liga tidak ditemukan");
                });

        // Validasi tim termasuk dalam liga
        if (!teamHome.getLiga().getIdLiga().equals(liga.getIdLiga())) {
            log.warn("⚠️ [PERTANDINGAN] Tim home tidak dalam liga | Team ID: {} | Liga ID: {}",
                    teamHome.getIdTeam(), liga.getIdLiga());
            throw new BadRequestException("Tim home tidak termasuk dalam liga ini");
        }

        if (!teamAway.getLiga().getIdLiga().equals(liga.getIdLiga())) {
            log.warn("⚠️ [PERTANDINGAN] Tim away tidak dalam liga | Team ID: {} | Liga ID: {}",
                    teamAway.getIdTeam(), liga.getIdLiga());
            throw new BadRequestException("Tim away tidak termasuk dalam liga ini");
        }

        // Cek jumlah pertandingan
        log.debug("🧮 [PERTANDINGAN] Menghitung pertandingan antara {} vs {}",
                teamHome.getNamaTeam(), teamAway.getNamaTeam());
        int jumlahPertandingan = pertandinganRepository.countPertandinganBetweenTeams(
                teamHome.getIdTeam(), teamAway.getIdTeam());

        if (jumlahPertandingan >= 2) {
            log.warn("⛔ [PERTANDINGAN] Sudah bertanding 2x | {} vs {}",
                    teamHome.getNamaTeam(), teamAway.getNamaTeam());
            throw new BadRequestException("Tim " + teamHome.getNamaTeam() + " dan " +
                    teamAway.getNamaTeam() + " sudah bertanding 2 kali");
        }

        // Proses hasil pertandingan
        String resultMessage;
        if (request.getSkorHome() > request.getSkorAway()) {
            // Home menang
            teamHome.setPoin(teamHome.getPoin() + 3);
            teamHome.setJumlahMenang(teamHome.getJumlahMenang() + 1);
            teamAway.setJumlahKalah(teamAway.getJumlahKalah() + 1);
            resultMessage = "Pertandingan dimenangkan oleh " + teamHome.getNamaTeam();
            log.info("🥇 [PERTANDINGAN] {} menang | Skor: {}-{}",
                    teamHome.getNamaTeam(), request.getSkorHome(), request.getSkorAway());
        } else if (request.getSkorHome() < request.getSkorAway()) {
            // Away menang
            teamAway.setPoin(teamAway.getPoin() + 3);
            teamAway.setJumlahMenang(teamAway.getJumlahMenang() + 1);
            teamHome.setJumlahKalah(teamHome.getJumlahKalah() + 1);
            resultMessage = "Pertandingan dimenangkan oleh " + teamAway.getNamaTeam();
            log.info("🥇 [PERTANDINGAN] {} menang | Skor: {}-{}",
                    teamAway.getNamaTeam(), request.getSkorAway(), request.getSkorHome());
        } else {
            // Seri
            teamHome.setPoin(teamHome.getPoin() + 1);
            teamAway.setPoin(teamAway.getPoin() + 1);
            teamHome.setJumlahImbang(teamHome.getJumlahImbang() + 1);
            teamAway.setJumlahImbang(teamAway.getJumlahImbang() + 1);
            resultMessage = "Pertandingan berakhir imbang";
            log.info("🤝 [PERTANDINGAN] Imbang | Skor: {}-{}",
                    request.getSkorHome(), request.getSkorAway());
        }

        // Simpan perubahan tim
        teamRepository.saveAll(List.of(teamHome, teamAway));
        log.debug("💾 [PERTANDINGAN] Poin tim berhasil diperbarui");

        // Buat pertandingan baru
        Pertandingan pertandingan = new Pertandingan();
        pertandingan.setTeamHome(teamHome);
        pertandingan.setTeamAway(teamAway);
        pertandingan.setSkorHome(request.getSkorHome());
        pertandingan.setSkorAway(request.getSkorAway());
        pertandingan.setLiga(liga);

        pertandingan = pertandinganRepository.save(pertandingan);
        log.info("✅ [PERTANDINGAN] Pertandingan tersimpan | ID: {} | {} vs {} {}-{}",
                pertandingan.getIdPertandingan(),
                teamHome.getNamaTeam(),
                teamAway.getNamaTeam(),
                pertandingan.getSkorHome(),
                pertandingan.getSkorAway());

        // Buat response
        PertandinganResponse response = new PertandinganResponse();
        response.setIdPertandingan(pertandingan.getIdPertandingan());
        response.setIdTeamHome(teamHome.getIdTeam());
        response.setNamaTeamHome(teamHome.getNamaTeam());
        response.setIdTeamAway(teamAway.getIdTeam());
        response.setNamaTeamAway(teamAway.getNamaTeam());
        response.setSkorHome(pertandingan.getSkorHome());
        response.setSkorAway(pertandingan.getSkorAway());
        response.setIdLiga(liga.getIdLiga());
        response.setMessage(resultMessage);

        log.debug("📤 [PERTANDINGAN] Response: {}", response);
        return response;
    }

    public ResponseEntity<?> checkMatch(Long idLiga) {
        log.info("🔍 [CHECK-MATCH] Memeriksa pertandingan liga ID: {}", idLiga);

        List<Team> teams = teamRepository.findByLigaIdLiga(idLiga);
        if (teams.isEmpty()) {
            log.warn("⚠️ [CHECK-MATCH] Liga kosong | ID: {}", idLiga);
            throw new NotFoundException("Tidak ada team didalam liga ini");
        }

        List<Map<String, String>> result = new ArrayList<>();
        boolean semuaPertandinganSelesai = true;
        Set<Long> teamsWithRemainingMatches = new HashSet<>(); // Untuk melacak tim yang masih ada pertandingan

        log.info("🧮 [CHECK-MATCH] Memeriksa {} tim di liga", teams.size());
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                Team team1 = teams.get(i);
                Team team2 = teams.get(j);

                if (team1.getIdTeam().equals(team2.getIdTeam())) continue;

                int jumlahPertandingan = pertandinganRepository.countPertandinganBetweenTeams(
                        team1.getIdTeam(), team2.getIdTeam());

                if (jumlahPertandingan < 2) {
                    Map<String, String> pair = new HashMap<>();
                    pair.put("idTeam1", team1.getIdTeam().toString());
                    pair.put("namaTeam1", team1.getNamaTeam());
                    pair.put("idTeam2", team2.getIdTeam().toString());
                    pair.put("namaTeam2", team2.getNamaTeam());
                    pair.put("sisaPertandingan", String.valueOf(2 - jumlahPertandingan));
                    result.add(pair);

                    semuaPertandinganSelesai = false;
                    teamsWithRemainingMatches.add(team1.getIdTeam());
                    teamsWithRemainingMatches.add(team2.getIdTeam());

                    log.info("⚽ [CHECK-MATCH] Pertandingan tersisa: {} vs {} ({}x)",
                            team1.getNamaTeam(), team2.getNamaTeam(), 2 - jumlahPertandingan);
                }
            }
        }

        if (semuaPertandinganSelesai) {
            log.info("✅ [CHECK-MATCH] SEMUA PERTANDINGAN SELESAI | Liga ID: {}", idLiga);
            return ResponseEntity.ok(Map.of("message", "Pertandingan di liga ini sudah selesai"));
        }

        // Log jumlah tim yang masih ada pertandingan
        log.info("📊 [CHECK-MATCH] STATISTIK: {} pertandingan tersisa | {} tim masih memiliki jadwal pertandingan | Liga ID: {}",
                result.size(),
                teamsWithRemainingMatches.size(),
                idLiga);

        return ResponseEntity.ok(result);
    }
}
