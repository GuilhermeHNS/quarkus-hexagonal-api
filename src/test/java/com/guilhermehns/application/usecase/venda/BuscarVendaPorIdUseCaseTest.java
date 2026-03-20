package com.guilhermehns.application.usecase.venda;

import com.guilhermehns.application.exception.VendaNaoEncontradaException;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BuscarVendaPorIdUseCaseTest {
    private VendaRepository repository;
    private BuscarVendaPorIdUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(VendaRepository.class);
        useCase = new BuscarVendaPorIdUseCase(repository);
    }

    @Test
    void deveBuscarVendaPorIdECalcularTotaisComSucesso() {
        UUID vendaId = UUID.randomUUID();
        Venda venda = criarVendaValidaComDoisItens();

        Mockito.when(repository.findById(vendaId)).thenReturn(Optional.of(venda));

        Venda resultado = useCase.executar(vendaId);

        assertEquals(new BigDecimal("250.00"), resultado.getValorTotalProdutos());
        assertEquals(new BigDecimal("22.50"), resultado.getImposto());
        assertEquals(new BigDecimal("272.50"), resultado.getValorTotalVenda());

        Mockito.verify(repository, Mockito.times(1)).findById(vendaId);
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoVendaNaoForEncontrada() {
        UUID vendaId = UUID.randomUUID();

        Mockito.when(repository.findById(vendaId)).thenReturn(Optional.empty());

        assertThrows(VendaNaoEncontradaException.class, () -> useCase.executar(vendaId));

        Mockito.verify(repository, Mockito.times(1)).findById(vendaId);
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
