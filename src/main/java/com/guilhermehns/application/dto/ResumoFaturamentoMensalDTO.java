package com.guilhermehns.application.dto;

import java.math.BigDecimal;

public class ResumoFaturamentoMensalDTO {
    private BigDecimal faturamentoLoja;
    private BigDecimal impostoTotal;
    private BigDecimal faturamentoMaisImposto;

    public BigDecimal getFaturamentoLoja() {
        return faturamentoLoja;
    }

    public void setFaturamentoLoja(BigDecimal faturamentoLoja) {
        this.faturamentoLoja = faturamentoLoja;
    }

    public BigDecimal getImpostoTotal() {
        return impostoTotal;
    }

    public void setImpostoTotal(BigDecimal impostoTotal) {
        this.impostoTotal = impostoTotal;
    }

    public BigDecimal getFaturamentoMaisImposto() {
        return faturamentoMaisImposto;
    }

    public void setFaturamentoMaisImposto(BigDecimal faturamentoMaisImposto) {
        this.faturamentoMaisImposto = faturamentoMaisImposto;
    }
}
