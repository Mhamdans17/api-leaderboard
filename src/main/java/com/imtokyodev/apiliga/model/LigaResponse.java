package com.imtokyodev.apiliga.model;

import lombok.Data;

@Data
public class LigaResponse {
    private Long idLiga;
    private String namaLiga;
    private Integer jumlahTeam;
    private String deskripsiLiga;
    private String message;
}
