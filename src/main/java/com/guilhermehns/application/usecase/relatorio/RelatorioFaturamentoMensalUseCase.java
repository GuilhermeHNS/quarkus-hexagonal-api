package com.guilhermehns.application.usecase.relatorio;

import com.guilhermehns.application.dto.FaturamentoMensalDTO;
import com.guilhermehns.domain.model.venda.ItemVenda;
import com.guilhermehns.domain.repository.VendaRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class RelatorioFaturamentoMensalUseCase {

    private final VendaRepository vendaRepository;

    public RelatorioFaturamentoMensalUseCase(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    public List<FaturamentoMensalDTO> executar(String dataReferencia) {

        LocalDate dataBase = LocalDate.parse(dataReferencia);

        return vendaRepository.buscaFaturamentoMensal(dataBase);
    }

    private BigDecimal calcularValorItem(ItemVenda item) {
        return item.getValorUnitario().multiply(BigDecimal.valueOf(item.getQuantidade()));
    }
}