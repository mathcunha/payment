package com.bank.payment.service;

import com.github.javafaker.Faker;
import com.bank.payment.domain.ArquivoCnab;
import com.bank.payment.domain.Lote;
import com.bank.payment.factory.PagamentoFactory;
import com.bank.payment.repository.ArquivoCnabRepository;
import com.bank.payment.repository.LoteRepository;
import com.bank.payment.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArquivoCnabService {

    @Autowired
    ArquivoCnabRepository repository;

    @Autowired
    LoteRepository loteRepo;

    @Autowired
    PagamentoRepository pagamentoRepo;

    public ArquivoCnab save(ArquivoCnab file){
        ArquivoCnab persisted = repository.save(file);
        Faker faker = new Faker();

        for (int i = 0; i < file.getNome().length(); i++) {
            Lote lote = loteRepo.save(PagamentoFactory.getLote(persisted));

            for (int j = 0; j < faker.number().numberBetween(5, 50); j++) {
                pagamentoRepo.save(PagamentoFactory.getPagamento(lote));
            }
        }

        return persisted;
    }

    public Optional<ArquivoCnab> findById(Long id){
        return repository.findById(id);
    }

}
