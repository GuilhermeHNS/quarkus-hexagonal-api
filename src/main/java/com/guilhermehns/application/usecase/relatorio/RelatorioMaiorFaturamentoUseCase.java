package com.guilhermehns.application.usecase.relatorio;

import com.guilhermehns.application.dto.ItemMaiorFaturamentoDTO;
import com.guilhermehns.domain.model.venda.ItemVenda;
import com.guilhermehns.domain.repository.VendaRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@ApplicationScoped
public class RelatorioMaiorFaturamentoUseCase {
    private final VendaRepository repository;

    public RelatorioMaiorFaturamentoUseCase(VendaRepository repository) {
        this.repository = repository;
    }

    public List<ItemMaiorFaturamentoDTO> executar() {
        return repository.findAllVendas().stream()
                .flatMap(venda -> venda.getItens().stream())
                .sorted(Comparator.comparing(this::calcularFaturamento).reversed())
                .limit(4)
                .map(item -> new ItemMaiorFaturamentoDTO(
                        item.getProduto().getId(),
                        item.getProduto().getNome(),
                        item.getProduto().getPrecoVenda(),
                        calcularFaturamento(item)
                ))
                .toList();
    }

    private BigDecimal calcularFaturamento(ItemVenda item) {
        return item.getValorUnitario().multiply(BigDecimal.valueOf(item.getQuantidade()));
    }
}
