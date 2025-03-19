package com.imtokyodev.apiliga.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PertandinganRequest {

    @NotNull(message = "ID tim home tidak boleh kosong")
    private Long idTeamHome;

    @NotNull(message = "ID tim away tidak boleh kosong")
    private Long idTeamAway;

    @NotNull(message = "Skor home tidak boleh kosong")
    private Integer skorHome;

    @NotNull(message = "Skor away tidak boleh kosong")
    private Integer skorAway;

    @NotNull(message = "ID liga tidak boleh kosong")
    private Long idLiga;
}
