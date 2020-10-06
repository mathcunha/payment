package com.bank.payment.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${spring.data.rest.basePath}/localFiles")
@CrossOrigin
public class LocalFileController {
    @Value("${lote.processor.dir}")
    private String diretorio;

    @GetMapping( produces = "application/json")
    public ResponseEntity listFiles() {
        File[] localDir = (new File(diretorio)).listFiles();
        Map files = new HashMap<String, String>();

        for (int i = 0; i < localDir.length; i++) {
            files.put(localDir[i].getName(), localDir[i].getAbsolutePath());
        }
        return ResponseEntity.ok(files);
    }
}
