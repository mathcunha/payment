package com.bank.payment.domain;

import com.bank.payment.enums.ArquivoCipStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "lotes")
@EntityListeners(AuditingEntityListener.class)
public class Lote extends Base{

    @Column(nullable = false)
    private ArquivoCipStatus status;

    @OneToOne(mappedBy = "lote")
    private ArquivoCip arquivoCip;

    @ManyToOne
    @NotNull
    @JsonIgnore
    private ArquivoCnab arquivoCnab;

    @OneToMany(mappedBy = "lote")
    private List<Pagamento> pagamentos;

    public Lote() {
    }

    public Lote(ArquivoCipStatus status, @NotNull ArquivoCnab arquivoCnab) {
        this.status = status;
        this.arquivoCnab = arquivoCnab;
    }

    public ArquivoCipStatus getStatus() {
        return status;
    }

    public void setStatus(ArquivoCipStatus status) {
        this.status = status;
    }

    public ArquivoCip getArquivoCip() {
        return arquivoCip;
    }

    public void setArquivoCip(ArquivoCip arquivoCip) {
        this.arquivoCip = arquivoCip;
    }

    public ArquivoCnab getArquivoCnab() {
        return arquivoCnab;
    }

    public void setArquivoCnab(ArquivoCnab arquivoCnab) {
        this.arquivoCnab = arquivoCnab;
    }

    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(List<Pagamento> pagamentos) {
        this.pagamentos = pagamentos;
    }
}
