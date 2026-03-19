package com.guilhermehns.application.usecase.relatorio;

import com.guilhermehns.application.dto.FaturamentoMensalDTO;
import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.model.venda.ItemVenda;
import com.guilhermehns.domain.model.venda.Venda;
import com.guilhermehns.domain.repository.VendaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RelatorioFaturamentoMensalUseCaseTest {
    @Test
    void deveRetornarFaturamentoMensalComImpostoELiquido() {
        VendaRepository vendaRepository = Mockito.mock(VendaRepository.class);

        RelatorioFaturamentoMensalUseCase useCase = new RelatorioFaturamentoMensalUseCase(vendaRepository);

        Produto produto1 = new Produto();
        produto1.setId(UUID.randomUUID());

        Produto produto2 = new Produto();
        produto2.setId(UUID.randomUUID());

        ItemVenda item1 = new ItemVenda();
        item1.setProduto(produto1);
        item1.setQuantidade(2);
        item1.setValorUnitario(new BigDecimal("100.00")); // 200

        ItemVenda item2 = new ItemVenda();
        item2.setProduto(produto2);
        item2.setQuantidade(3);
        item2.setValorUnitario(new BigDecimal("50.00")); // 150

        Venda vendaMarco1 = new Venda();
        vendaMarco1.setDataVenda(LocalDateTime.of(2026, 3, 10, 10, 0));
        vendaMarco1.setItens(List.of(item1));

        Venda vendaMarco2 = new Venda();
        vendaMarco2.setDataVenda(LocalDateTime.of(2026, 3, 15, 14, 0));
        vendaMarco2.setItens(List.of(item2));

        Venda vendaAbril = new Venda();
        vendaAbril.setDataVenda(LocalDateTime.of(2026, 4, 1, 9, 0));
        vendaAbril.setItens(List.of(item1));

        Mockito.when(vendaRepository.findAllVendas())
                .thenReturn(List.of(vendaMarco1, vendaMarco2, vendaAbril));

        FaturamentoMensalDTO resultado = useCase.executar(3);

        assertEquals(new BigDecimal("350.00"), resultado.getFaturamentoBruto());
        assertEquals(new BigDecimal("31.50"), resultado.getImposto());
        assertEquals(new BigDecimal("318.50"), resultado.getFaturamentoLiquido());

        Mockito.verify(vendaRepository).findAllVendas();
    }
}
