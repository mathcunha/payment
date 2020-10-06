package com.bank.payment.enums;

public enum ArquivoCipStatus {


    IMPORTADO("IMPORTADO"),
    EMPACOTADO("EMPACOTADO"),
    ;


    private String status;

    ArquivoCipStatus(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}