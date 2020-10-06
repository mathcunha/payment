package com.bank.payment.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "arquivos_cnab", uniqueConstraints={@UniqueConstraint(columnNames={"nome"}, name = "UK_arquivoCnab_nome")})
@EntityListeners(AuditingEntityListener.class)
public class ArquivoCnab extends Base{

    @Column(length = 300, nullable = false)
    @Size(max = 300)
    @NotBlank
    private String nome;

    @OneToMany(mappedBy = "arquivoCnab")
    private List<Lote> lotes;
    
    public ArquivoCnab(){

    }

    public ArquivoCnab(@Size(max = 300) @NotBlank String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Lote> getLotes() {
        return lotes;
    }

    public void setLotes(List<Lote> lotes) {
        this.lotes = lotes;
    }
}
