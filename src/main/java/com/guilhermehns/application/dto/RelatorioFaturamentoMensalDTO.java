package com.guilhermehns.application.dto;

import java.util.List;

public class RelatorioFaturamentoMensalDTO {
    private List<FaturamentoMensalItemDTO> faturamentosMensais;
    private ResumoFaturamentoMensalDTO resumo;

    public List<FaturamentoMensalItemDTO> getFaturamentosMensais() {
        return faturamentosMensais;
    }

    public void setFaturamentosMensais(List<FaturamentoMensalItemDTO> faturamentosMensais) {
        this.faturamentosMensais = faturamentosMensais;
    }

    public ResumoFaturamentoMensalDTO getResumo() {
        return resumo;
    }

    public void setResumo(ResumoFaturamentoMensalDTO resumo) {
        this.resumo = resumo;
    }
}
