package com.bank.payment.step;

import com.bank.payment.domain.ArquivoCip;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;


@Component
public class ArquivoCipFileWriter implements ItemWriter<ArquivoCip> {
    private static final Logger log = LoggerFactory.getLogger(ArquivoCipFileWriter.class);

    @Value("${lote.processor.dir}")
    private String diretorio;

    @Override
    public void write(List<? extends ArquivoCip> list) throws Exception {
        StreamFactory factory = StreamFactory.newInstance();
        factory.loadResource("mapeamento_cnab240.xml");

        for (ArquivoCip arquivo: list) {
            String file = diretorio + File.separator +arquivo.getName();
            OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file), Charset.forName("ISO-8859-1"));
            BeanWriter out = factory.createWriter("file_cnab240_strem", fileWriter);
            out.write(arquivo);
            out.write(arquivo.getLote());

            arquivo.getLote().getPagamentos().forEach(p -> out.write(p));

            out.flush();
            fileWriter.close();
        }


    }
}
