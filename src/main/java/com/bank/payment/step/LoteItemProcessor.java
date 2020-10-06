package com.bank.payment.step;

import com.bank.payment.domain.ArquivoCip;
import com.bank.payment.domain.Lote;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;

public class LoteItemProcessor implements ItemProcessor<Lote, ArquivoCip> {
    @Override
    public ArquivoCip process(Lote lote) throws Exception {
        ArquivoCip arquivoCip = new ArquivoCip(String.format("CIP-%s-%d", lote.getArquivoCnab().getNome(), System.currentTimeMillis()), lote);
        arquivoCip.setCreated(LocalDateTime.now());
        arquivoCip.setUpdated(LocalDateTime.now());
        arquivoCip.setTotal((float)lote.getPagamentos().stream().mapToDouble(p -> p.getValor()).sum());
        lote.setArquivoCip(arquivoCip);
        return arquivoCip;
    }
}
