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
    void deveRetornarProdutosComMaiorFaturamento() {
        VendaRepository vendaRepository = Mockito.mock(VendaRepository.class);
        RelatorioMaiorFaturamentoUseCase useCase = new RelatorioMaiorFaturamentoUseCase(vendaRepository);

        UUID produtoId1 = UUID.randomUUID();
        UUID produtoId2 = UUID.randomUUID();

        ItemMaiorFaturamentoDTO item1 = new ItemMaiorFaturamentoDTO();
        item1.setProdutoId(produtoId1);
        item1.setNomeProduto("Mouse Logitech Sem Fio");
        item1.setPrecoVenda(new BigDecimal("89.90"));
        item1.setFaturamento(new BigDecimal("42000.00"));

        ItemMaiorFaturamentoDTO item2 = new ItemMaiorFaturamentoDTO();
        item2.setProdutoId(produtoId2);
        item2.setNomeProduto("Notebook Dell Inspiron");
        item2.setPrecoVenda(new BigDecimal("3500.00"));
        item2.setFaturamento(new BigDecimal("719.20"));

        List<ItemMaiorFaturamentoDTO> retornoEsperado = List.of(item1, item2);

        Mockito.when(vendaRepository.buscarProdutosComMaiorFaturamento())
                .thenReturn(retornoEsperado);

        List<ItemMaiorFaturamentoDTO> resultado = useCase.executar();

        assertEquals(2, resultado.size());
        assertEquals(produtoId1, resultado.getFirst().getProdutoId());
        assertEquals("Mouse Logitech Sem Fio", resultado.get(0).getNomeProduto());
        assertEquals(new BigDecimal("42000.00"), resultado.get(0).getFaturamento());

        assertEquals(produtoId2, resultado.get(1).getProdutoId());
        assertEquals("Notebook Dell Inspiron", resultado.get(1).getNomeProduto());
        assertEquals(new BigDecimal("719.20"), resultado.get(1).getFaturamento());

        Mockito.verify(vendaRepository).buscarProdutosComMaiorFaturamento();
    }
}
