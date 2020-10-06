package com.bank.payment.agendador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobAgendador {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    @Qualifier("loteStep")
    private Step loteStep;

    @Autowired
    JobLauncher jobLauncher;

    private static final Logger log = LoggerFactory.getLogger(JobAgendador.class);

    @Scheduled(cron = "${lote.processor.crontab}")
    public void scheduleLotePagamentoJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        log.info("iniciado lotePagamentoJob");
        Job lotePagamentoJob = jobBuilderFactory.get("lotePagamentoJob")
                .incrementer(new RunIdIncrementer())
                .flow(loteStep)
                .end()
                .build();

        jobLauncher.run(lotePagamentoJob, new JobParametersBuilder().addLong("time",System.currentTimeMillis()).toJobParameters());
    }
}
