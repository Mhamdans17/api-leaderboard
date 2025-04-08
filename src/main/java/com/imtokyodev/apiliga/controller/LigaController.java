package com.imtokyodev.apiliga.controller;

import com.imtokyodev.apiliga.model.LigaRequest;
import com.imtokyodev.apiliga.model.LigaResponse;
import com.imtokyodev.apiliga.service.LigaService;
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
public class LigaController {

    private final LigaService ligaService;
    public LigaController(LigaService ligaService) {
        this.ligaService = ligaService;
    }

    @PostMapping("/add/liga")
    public ResponseEntity<?> createLiga(@Valid @RequestBody LigaRequest request) {
        LigaResponse response = ligaService.createLiga(request);
        return ResponseEntity.ok(response);
    }
}
