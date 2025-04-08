package com.imtokyodev.apiliga.service;

import com.imtokyodev.apiliga.entity.Liga;
import com.imtokyodev.apiliga.exception.BadRequestException;
import com.imtokyodev.apiliga.model.LigaRequest;
import com.imtokyodev.apiliga.model.LigaResponse;
import com.imtokyodev.apiliga.repository.LigaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LigaService {

    private final LigaRepository ligaRepository;
    public LigaService(LigaRepository ligaRepository) {
        this.ligaRepository = ligaRepository;
    }

    public LigaResponse createLiga(LigaRequest request) {
        log.info("🏟️ [LIGA] Memulai proses pembuatan liga baru | Request: {}", request);

        // Validasi input
        log.debug("🔍 [LIGA] Memvalidasi data input");
        if (request.getNamaLiga() == null || request.getJumlahTeam() == null || request.getDeskripsiLiga() == null) {
            log.error("❌ [LIGA] Gagal validasi | Data tidak lengkap | Nama: {} | Jumlah Team: {} | Deskripsi: {}",
                    request.getNamaLiga(),
                    request.getJumlahTeam(),
                    request.getDeskripsiLiga());
            throw new BadRequestException("Gagal menambahkan liga, data tidak lengkap");
        }

        // Membuat entitas liga
        log.debug("🛠️ [LIGA] Membuat entitas liga baru");
        Liga liga = new Liga();
        liga.setNamaLiga(request.getNamaLiga());
        liga.setJumlahTeam(request.getJumlahTeam());
        liga.setDeskripsiLiga(request.getDeskripsiLiga());

        // Menyimpan ke database
        log.debug("💾 [LIGA] Menyimpan liga ke database");
        try {
            liga = ligaRepository.save(liga);
            log.info("✅ [LIGA] Liga berhasil dibuat | ID: {} | Nama: '{}' | Jumlah Team: {}",
                    liga.getIdLiga(),
                    liga.getNamaLiga(),
                    liga.getJumlahTeam());
        } catch (Exception e) {
            log.error("❌ [LIGA] Gagal menyimpan liga | Nama: '{}' | Error: {}",
                    request.getNamaLiga(),
                    e.getMessage());
            throw new BadRequestException("Gagal menyimpan liga ke database");
        }

        // Membuat response
        log.debug("📤 [LIGA] Membuat response");
        LigaResponse response = new LigaResponse();
        response.setIdLiga(liga.getIdLiga());
        response.setNamaLiga(liga.getNamaLiga());
        response.setJumlahTeam(liga.getJumlahTeam());
        response.setDeskripsiLiga(liga.getDeskripsiLiga());
        response.setMessage("Liga berhasil ditambahkan");

        log.info("🎉 [LIGA] Proses selesai | Response: {}", response);
        return response;
    }
}
