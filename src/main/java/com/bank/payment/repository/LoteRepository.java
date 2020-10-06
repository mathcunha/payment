package com.bank.payment.repository;

import com.bank.payment.enums.ArquivoCipStatus;
import com.bank.payment.domain.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LoteRepository extends JpaRepository<Lote, Long> {
    @Modifying
    @Query("update Lote l set l.status = ?1 where l.id = ?2")
    int setStatusFor(ArquivoCipStatus status, Long roleId);
}
