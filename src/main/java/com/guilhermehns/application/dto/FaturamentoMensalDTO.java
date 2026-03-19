package com.guilhermehns.application.dto;

import java.math.BigDecimal;

public class FaturamentoMensalDTO {
    private BigDecimal faturamentoBruto;
    private BigDecimal imposto;
    private BigDecimal faturamentoLiquido;

    public FaturamentoMensalDTO() {
    }

    public FaturamentoMensalDTO(BigDecimal faturamentoBruto, BigDecimal imposto, BigDecimal faturamentoLiquido) {
        this.faturamentoBruto = faturamentoBruto;
        this.imposto = imposto;
        this.faturamentoLiquido = faturamentoLiquido;
    }

    public BigDecimal getFaturamentoBruto() {
        return faturamentoBruto;
    }

    public void setFaturamentoBruto(BigDecimal faturamentoBruto) {
        this.faturamentoBruto = faturamentoBruto;
    }

    public BigDecimal getImposto() {
        return imposto;
    }

    public void setImposto(BigDecimal imposto) {
        this.imposto = imposto;
    }

    public BigDecimal getFaturamentoLiquido() {
        return faturamentoLiquido;
    }

    public void setFaturamentoLiquido(BigDecimal faturamentoLiquido) {
        this.faturamentoLiquido = faturamentoLiquido;
    }
}
