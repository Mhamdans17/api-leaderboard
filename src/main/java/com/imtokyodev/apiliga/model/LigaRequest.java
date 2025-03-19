package com.imtokyodev.apiliga.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
public class LigaRequest {
    @NotBlank(message = "Nama liga tidak boleh kosong")
    private String namaLiga;

    @NotNull(message = "Jumlah team tiddak boleh kosong")
    private Integer jumlahTeam;

    @NotBlank(message = "Deskripsi liga tidak boleh kosong")
    private String deskripsiLiga;
}
