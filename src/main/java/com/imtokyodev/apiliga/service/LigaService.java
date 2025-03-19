package com.imtokyodev.apiliga.service;

import com.imtokyodev.apiliga.entity.Liga;
import com.imtokyodev.apiliga.exception.BadRequestException;
import com.imtokyodev.apiliga.model.LigaRequest;
import com.imtokyodev.apiliga.model.LigaResponse;
import com.imtokyodev.apiliga.repository.LigaRepository;
import org.springframework.stereotype.Service;

@Service
public class LigaService {

    private final LigaRepository ligaRepository;
    public LigaService(LigaRepository ligaRepository) {
        this.ligaRepository = ligaRepository;
    }

    public LigaResponse createLiga(LigaRequest request) {

        if (request.getNamaLiga() == null || request.getJumlahTeam() == null || request.getDeskripsiLiga() == null) {
            throw new BadRequestException("Gagal menambahkan liga, data tidak lengkap");
        }

        Liga liga = new Liga();
        liga.setNamaLiga(request.getNamaLiga());
        liga.setJumlahTeam(request.getJumlahTeam());
        liga.setDeskripsiLiga(request.getDeskripsiLiga());

        liga = ligaRepository.save(liga);

        LigaResponse response = new LigaResponse();
        response.setIdLiga(liga.getIdLiga());
        response.setNamaLiga(liga.getNamaLiga());
        response.setJumlahTeam(liga.getJumlahTeam());
        response.setDeskripsiLiga(liga.getDeskripsiLiga());
        response.setMessage("Liga berhasil ditambahkan");

        return response;
    }
}
