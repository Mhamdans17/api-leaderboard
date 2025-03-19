package com.imtokyodev.apiliga.model;

import lombok.Data;

@Data
public class TeamResponse {
    private Long idTeam;
    private String namaTeam;
    private Long idLiga;
    private String namaLiga;
    private String message;
}
