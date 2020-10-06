package com.bank.payment.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "arquivos_cip")
public class ArquivoCip extends Base{

    @NotBlank
    @Size(max = 150)
    @Column(length = 150)
    private String name;

    @NotNull
    private Float total;

    @OneToOne
    @NotNull
    @JsonIgnore
    private Lote lote;

    public ArquivoCip(@NotBlank @Size(max = 150) String name, @NotNull Lote lote) {
        this.name = name;
        this.lote = lote;
    }

    public ArquivoCip() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }
}
