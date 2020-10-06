package com.bank.payment.controller;

import com.bank.payment.service.ArquivoCnabService;
import com.bank.payment.domain.ArquivoCnab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("${spring.data.rest.basePath}/arquivosCnab")
@CrossOrigin
public class ArquivoCnabController {

    @Autowired
    ArquivoCnabService service;

    @Value("${spring.data.rest.basePath}")
    private String basePath;

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ArquivoCnab> create(@Valid @RequestBody ArquivoCnab resource) {
        ArquivoCnab persisted = service.save(resource);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(persisted.getId())
                .toUri();
        return ResponseEntity.created(uri)
                .eTag(Long.toString(persisted.getVersion()))
                .body(persisted);
    }


    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity findById(@PathVariable Long id, @RequestParam(required = false) String projection) {
            return ResponseEntity.of(service.findById(id));
    }

}
