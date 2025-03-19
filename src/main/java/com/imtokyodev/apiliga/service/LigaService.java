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
        log.info("Received request to create Liga with name: {}", request.getNamaLiga());

        if (request.getNamaLiga() == null || request.getJumlahTeam() == null || request.getDeskripsiLiga() == null) {
            throw new BadRequestException("Gagal menambahkan liga, data tidak lengkap");
        }

        Liga liga = new Liga();
        liga.setNamaLiga(request.getNamaLiga());
        liga.setJumlahTeam(request.getJumlahTeam());
        liga.setDeskripsiLiga(request.getDeskripsiLiga());

        liga = ligaRepository.save(liga);
        log.info("Liga with name '{}' successfully saved with ID: {}", liga.getNamaLiga(), liga.getIdLiga());

        LigaResponse response = new LigaResponse();
        response.setIdLiga(liga.getIdLiga());
        response.setNamaLiga(liga.getNamaLiga());
        response.setJumlahTeam(liga.getJumlahTeam());
        response.setDeskripsiLiga(liga.getDeskripsiLiga());
        response.setMessage("Liga berhasil ditambahkan");

        log.info("Returning response: {}", response);

        return response;
    }
}
