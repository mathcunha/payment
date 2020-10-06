package com.bank.payment.step;

import com.bank.payment.domain.ArquivoCip;
import com.bank.payment.domain.Lote;
import com.bank.payment.enums.ArquivoCipStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class LotePagamentoJob {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private ArquivoCipJpaWriter arquivoCipJpaWriter;
    @Autowired
    private ArquivoCipFileWriter arquivoCipFileWriter;

    @Bean
    public JpaPagingItemReader itemReader() {
        Map parameters = new HashMap<String, Object>();
        parameters.put("status", ArquivoCipStatus.IMPORTADO);
        return new JpaPagingItemReaderBuilder<Lote>()
                .name("loteReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select l from Lote l where l.status = :status order by l.id")
                .parameterValues(parameters)
                .pageSize(8)
                .build();
    }

    @Bean
    public LoteItemProcessor processor() {
        return new LoteItemProcessor();
    }

    @Bean
    public Step loteStep(PlatformTransactionManager transactionManager) {
        CompositeItemWriter compositeItemWriter = new CompositeItemWriter<ArquivoCip>();
        List itemWriters = new ArrayList();
        itemWriters.add(arquivoCipJpaWriter);
        itemWriters.add(arquivoCipFileWriter);
        compositeItemWriter.setDelegates(itemWriters);

        return this.stepBuilderFactory.get("loteStep")
                .transactionManager(transactionManager)
                .<String, String>chunk(2)
                .reader(itemReader())
                .processor(processor())
                .writer(compositeItemWriter)
                .build();
    }

}
