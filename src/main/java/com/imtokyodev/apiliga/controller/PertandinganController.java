package com.imtokyodev.apiliga.controller;

import com.imtokyodev.apiliga.model.PertandinganRequest;
import com.imtokyodev.apiliga.model.PertandinganResponse;
import com.imtokyodev.apiliga.service.PertandinganService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class PertandinganController {

    private final PertandinganService pertandinganService;
    public PertandinganController(PertandinganService pertandinganService) {
        this.pertandinganService = pertandinganService;
    }

    @PostMapping("/add/pertandingan")
    public ResponseEntity<?> mulaiPertandingan(@Valid @RequestBody PertandinganRequest request) {
        PertandinganResponse response = pertandinganService.getPertandingan(request);
        log.info("Response Match {}", response.toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-match/{idLiga}")
    public ResponseEntity<?> getTimYangBelumBertemu2Kali(@PathVariable Long idLiga) {
        return pertandinganService.checkMatch(idLiga);
    }

}
