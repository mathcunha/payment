package com.bank.payment.factory;

import com.bank.payment.domain.Pagamento;
import com.bank.payment.enums.ArquivoCipStatus;
import com.github.javafaker.Faker;
import com.bank.payment.domain.ArquivoCnab;
import com.bank.payment.domain.Lote;

public class PagamentoFactory {

    private static Faker faker = new Faker();

    public static Lote getLote(ArquivoCnab arquivo){
        return new Lote(ArquivoCipStatus.IMPORTADO, arquivo);
    }

    public static Pagamento getPagamento(Lote lote){
        return new Pagamento((float)faker.number().randomDouble(2,1,5), faker.number().randomDigit(), faker.number().numberBetween(1000, 10000), lote);
    }
}
