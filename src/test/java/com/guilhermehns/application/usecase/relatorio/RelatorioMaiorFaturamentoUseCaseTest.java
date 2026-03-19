package com.guilhermehns.application.usecase.relatorio;

import com.guilhermehns.application.dto.ItemMaiorFaturamentoDTO;
import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.model.venda.ItemVenda;
import com.guilhermehns.domain.model.venda.Venda;
import com.guilhermehns.domain.repository.VendaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RelatorioMaiorFaturamentoUseCaseTest {
    @Test
    void deveRetornarOs4ItensDeMaiorFaturamento() {
        VendaRepository vendaRepository = Mockito.mock(VendaRepository.class);

        RelatorioMaiorFaturamentoUseCase useCase = new RelatorioMaiorFaturamentoUseCase(vendaRepository);

        Produto produto1 = new Produto();
        produto1.setId(UUID.randomUUID());
        produto1.setNome("Produto 1");
        produto1.setPrecoVenda(new BigDecimal("10.00"));

        Produto produto2 = new Produto();
        produto2.setId(UUID.randomUUID());
        produto2.setNome("Produto 2");
        produto2.setPrecoVenda(new BigDecimal("20.00"));

        Produto produto3 = new Produto();
        produto3.setId(UUID.randomUUID());
        produto3.setNome("Produto 3");
        produto3.setPrecoVenda(new BigDecimal("30.00"));

        Produto produto4 = new Produto();
        produto4.setId(UUID.randomUUID());
        produto4.setNome("Produto 4");
        produto4.setPrecoVenda(new BigDecimal("40.00"));

        Produto produto5 = new Produto();
        produto5.setId(UUID.randomUUID());
        produto5.setNome("Produto 5");
        produto5.setPrecoVenda(new BigDecimal("50.00"));

        ItemVenda item1 = new ItemVenda(produto1, 2, new BigDecimal("10.00")); // 20
        ItemVenda item2 = new ItemVenda(produto2, 3, new BigDecimal("20.00")); // 60
        ItemVenda item3 = new ItemVenda(produto3, 4, new BigDecimal("30.00")); // 120
        ItemVenda item4 = new ItemVenda(produto4, 5, new BigDecimal("40.00")); // 200
        ItemVenda item5 = new ItemVenda(produto5, 6, new BigDecimal("50.00")); // 300

        Venda venda = new Venda();
        venda.setItens(List.of(item1, item2, item3, item4, item5));

        Mockito.when(vendaRepository.findAllVendas())
                .thenReturn(List.of(venda));

        List<ItemMaiorFaturamentoDTO> resultado = useCase.executar();

        assertEquals(4, resultado.size());
        assertEquals(produto5.getId(), resultado.get(0).getProdutoId());
        assertEquals(produto4.getId(), resultado.get(1).getProdutoId());
        assertEquals(produto3.getId(), resultado.get(2).getProdutoId());
        assertEquals(produto2.getId(), resultado.get(3).getProdutoId());

        Mockito.verify(vendaRepository).findAllVendas();
    }
}
