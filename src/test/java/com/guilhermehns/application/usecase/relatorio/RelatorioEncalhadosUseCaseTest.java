package com.guilhermehns.application.usecase.relatorio;

import com.guilhermehns.application.dto.EncalhadoDTO;
import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.model.venda.ItemVenda;
import com.guilhermehns.domain.model.venda.Venda;
import com.guilhermehns.domain.repository.ProdutoRepository;
import com.guilhermehns.domain.repository.VendaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RelatorioEncalhadosUseCaseTest {
    @Test
    void deveRetornarProdutosSemVendaHaMaisDe30Dias() {
        ProdutoRepository produtoRepository = Mockito.mock(ProdutoRepository.class);
        VendaRepository vendaRepository = Mockito.mock(VendaRepository.class);

        RelatorioEncalhadosUseCase useCase = new RelatorioEncalhadosUseCase(produtoRepository, vendaRepository);

        Produto produto1 = new Produto();
        produto1.setId(UUID.randomUUID());
        produto1.setNome("Produto Encalhado");
        produto1.setDataCadastro(LocalDateTime.of(2026, 1, 10, 10, 0));

        Produto produto2 = new Produto();
        produto2.setId(UUID.randomUUID());
        produto2.setNome("Produto Vendido Recentemente");
        produto2.setDataCadastro(LocalDateTime.of(2026, 1, 15, 10, 0));

        ItemVenda item = new ItemVenda();
        item.setProduto(produto2);
        item.setQuantidade(1);
        item.setValorUnitario(new BigDecimal("100.00"));

        Venda vendaRecente = new Venda();
        vendaRecente.setDataVenda(LocalDateTime.now().minusDays(10));
        vendaRecente.setItens(List.of(item));

        Mockito.when(produtoRepository.findAllProdutos())
                .thenReturn(List.of(produto1, produto2));

        Mockito.when(vendaRepository.findAllVendas())
                .thenReturn(List.of(vendaRecente));

        List<EncalhadoDTO> resultado = useCase.executar();

        assertEquals(1, resultado.size());
        assertEquals(produto1.getId(), resultado.get(0).getProdutoId());
        assertEquals("Produto Encalhado", resultado.get(0).getNomeProduto());

        Mockito.verify(produtoRepository).findAllProdutos();
        Mockito.verify(vendaRepository).findAllVendas();
    }
}
