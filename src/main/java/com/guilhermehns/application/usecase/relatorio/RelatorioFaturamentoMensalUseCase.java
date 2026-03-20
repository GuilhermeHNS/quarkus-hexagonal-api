package com.guilhermehns.application.usecase.relatorio;

import com.guilhermehns.application.dto.RelatorioFaturamentoMensalDTO;
import com.guilhermehns.domain.model.venda.ItemVenda;
import com.guilhermehns.domain.repository.VendaRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.time.LocalDate;

@ApplicationScoped
public class RelatorioFaturamentoMensalUseCase {

    private final VendaRepository vendaRepository;

    public RelatorioFaturamentoMensalUseCase(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    public RelatorioFaturamentoMensalDTO executar(String dataReferencia) {

        LocalDate dataBase = LocalDate.parse(dataReferencia);

        return vendaRepository.buscaFaturamentoMensal(dataBase);
    }

    private BigDecimal calcularValorItem(ItemVenda item) {
        return item.getValorUnitario().multiply(BigDecimal.valueOf(item.getQuantidade()));
    }
}