package com.imtokyodev.apiliga.repository;

import com.imtokyodev.apiliga.entity.Liga;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LigaRepository extends JpaRepository<Liga, Long> {
}
