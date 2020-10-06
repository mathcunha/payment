package com.bank.payment.step;

import com.bank.payment.domain.ArquivoCip;
import com.bank.payment.enums.ArquivoCipStatus;
import com.bank.payment.repository.LoteRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

@Component
public class ArquivoCipJpaWriter implements ItemWriter<ArquivoCip> {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private LoteRepository loteRepository;

    @Override
    public void write(List<? extends ArquivoCip> arquivoCips) throws Exception {
        arquivoCips.forEach(l -> {
            entityManager.persist(l);
            loteRepository.setStatusFor(ArquivoCipStatus.EMPACOTADO, l.getLote().getId());
        });
    }
}
