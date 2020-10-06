package com.bank.payment.enums;

public enum ArquivoCnabStatus {

    INVALIDO("INVALIDO"),
    IMPORTANDO("IMPORTANDO"),
    IMPORTADO("IMPORTADO"),
    EM_PROCESSAMENTO("EM_PROCESSAMENTO"),
    PROCESSADO("PROCESSADO"),
    ;

    private String status;

    ArquivoCnabStatus(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}