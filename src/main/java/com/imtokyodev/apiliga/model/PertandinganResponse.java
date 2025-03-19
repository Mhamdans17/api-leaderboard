package com.imtokyodev.apiliga.model;

import lombok.Data;

@Data
public class PertandinganResponse {

    private Long idPertandingan;
    private Long idTeamHome;
    private String namaTeamHome;
    private Long idTeamAway;
    private String namaTeamAway;
    private Integer skorHome;
    private Integer skorAway;
    private Long idLiga;
    private String message;
}
