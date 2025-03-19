package com.imtokyodev.apiliga.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TeamRequest {
    @NotBlank(message = "Nama tidak boleh kosong")
    private String namaTeam;

    private Long idLiga;
}
