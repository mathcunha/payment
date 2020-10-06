package com.bank.payment.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "pagamentos")
@EntityListeners(AuditingEntityListener.class)
public class Pagamento extends Base {

    @NotNull
    @Min(value = 0, message = "Size should not be less than 0")
    private Float valor;
    @NotNull
    private Integer conta;
    @NotNull
    private Integer agencia;

    @NotNull
    @ManyToOne
    @JsonIgnore
    private Lote lote;

    public Pagamento() {
    }

    public Pagamento(@NotNull @Min(value = 0, message = "Size should not be less than 0") Float valor, @NotNull Integer conta, @NotNull Integer agencia, @NotNull Lote lote) {
        this.valor = valor;
        this.conta = conta;
        this.agencia = agencia;
        this.lote = lote;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public Integer getConta() {
        return conta;
    }

    public void setConta(Integer conta) {
        this.conta = conta;
    }

    public Integer getAgencia() {
        return agencia;
    }

    public void setAgencia(Integer agencia) {
        this.agencia = agencia;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }
}