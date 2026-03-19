package com.guilhermehns.application.usecase.relatorio;

import com.guilhermehns.application.dto.FaturamentoMensalDTO;
import com.guilhermehns.domain.model.venda.ItemVenda;
import com.guilhermehns.domain.repository.VendaRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ApplicationScoped
public class RelatorioFaturamentoMensalUseCase {

    private final VendaRepository vendaRepository;

    public RelatorioFaturamentoMensalUseCase(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    public FaturamentoMensalDTO executar(int mes) {
        BigDecimal faturamentoBruto = vendaRepository.findAllVendas().stream()
                .filter(venda -> venda.getDataVenda() != null && venda.getDataVenda().getMonthValue() == mes)
                .flatMap(venda -> venda.getItens().stream())
                .map(this::calcularValorItem)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal imposto = faturamentoBruto
                .multiply(new BigDecimal("0.09"))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal faturamentoLiquido = faturamentoBruto.subtract(imposto)
                .setScale(2, RoundingMode.HALF_UP);

        return new FaturamentoMensalDTO(faturamentoBruto.setScale(2, RoundingMode.HALF_UP), imposto, faturamentoLiquido);
    }

    private BigDecimal calcularValorItem(ItemVenda item) {
        return item.getValorUnitario().multiply(BigDecimal.valueOf(item.getQuantidade()));
    }
}