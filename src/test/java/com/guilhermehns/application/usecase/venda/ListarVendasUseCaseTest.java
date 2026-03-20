package com.guilhermehns.application.usecase.venda;

import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.model.venda.ItemVenda;
import com.guilhermehns.domain.model.venda.Venda;
import com.guilhermehns.domain.repository.VendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListarVendasUseCaseTest {
    private VendaRepository repository;
    private ListarVendasUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(VendaRepository.class);
        useCase = new ListarVendasUseCase(repository);
    }

    @Test
    void deveCalcularTotaisDasVendasComSucesso() {
        Venda venda = criarVendaValidaComDoisItens();

        Mockito.when(repository.findAllVendas()).thenReturn(List.of(venda));

        List<Venda> resultado = useCase.executar();

        assertEquals(1, resultado.size());

        Venda vendaResultado = resultado.get(0);

        assertEquals(new BigDecimal("250.00"), vendaResultado.getValorTotalProdutos());
        assertEquals(new BigDecimal("22.50"), vendaResultado.getImposto());
        assertEquals(new BigDecimal("272.50"), vendaResultado.getValorTotalVenda());

        Mockito.verify(repository, Mockito.times(1)).findAllVendas();
        Mockito.verifyNoMoreInteractions(repository);
    }

    private Venda criarVendaValidaComDoisItens() {
        Produto produto1 = new Produto();
        produto1.setId(UUID.randomUUID());
        produto1.setNome("Notebook");

        Produto produto2 = new Produto();
        produto2.setId(UUID.randomUUID());
        produto2.setNome("Mouse");

        ItemVenda item1 = new ItemVenda();
        item1.setProduto(produto1);
        item1.setQuantidade(2);
        item1.setValorUnitario(new BigDecimal("100.00"));

        ItemVenda item2 = new ItemVenda();
        item2.setProduto(produto2);
        item2.setQuantidade(1);
        item2.setValorUnitario(new BigDecimal("50.00"));

        Venda venda = new Venda();
        venda.setId(UUID.randomUUID());
        venda.setCodigoVendedor("VEND001");
        venda.setDataVenda(LocalDateTime.now());
        venda.setFormaPagamento("DINHEIRO");
        venda.setValorPago(new BigDecimal("300.00"));
        venda.setItens(List.of(item1, item2));

        return venda;
    }
}
