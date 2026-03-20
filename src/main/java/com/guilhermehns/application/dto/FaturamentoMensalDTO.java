package com.guilhermehns.application.dto;

import java.math.BigDecimal;

public class FaturamentoMensalDTO {
    private BigDecimal faturamentoBruto;
    private BigDecimal imposto;
    private BigDecimal faturamentoLiquido;
    private int ano;
    private int mes;

    public FaturamentoMensalDTO() {
    }

    public FaturamentoMensalDTO(BigDecimal faturamentoBruto, BigDecimal imposto, BigDecimal faturamentoLiquido, int ano, int mes) {
        this.faturamentoBruto = faturamentoBruto;
        this.imposto = imposto;
        this.faturamentoLiquido = faturamentoLiquido;
        this.ano = ano;
        this.mes = mes;
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

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }
}
