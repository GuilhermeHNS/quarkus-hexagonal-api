package com.guilhermehns.application.usecase.venda;

import com.guilhermehns.domain.model.cliente.Cliente;
import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.model.venda.ItemVenda;
import com.guilhermehns.domain.model.venda.Venda;
import com.guilhermehns.domain.repository.VendaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AtualizarVendaUseCaseTest {
    @Test
    void deveAtualizarVendaComSucesso() {
        VendaRepository repository = Mockito.mock(VendaRepository.class);

        AtualizarVendaUseCase useCase = new AtualizarVendaUseCase(repository);

        UUID id = UUID.randomUUID();

        Cliente cliente = new Cliente();
        cliente.setId(UUID.randomUUID());

        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome("Produto Original");

        ItemVenda itemOriginal = new ItemVenda();
        itemOriginal.setProduto(produto);
        itemOriginal.setQuantidade(1);
        itemOriginal.setValorUnitario(new BigDecimal("50.00"));

        Venda vendaExistente = new Venda();
        vendaExistente.setId(id);
        vendaExistente.setCodigoVendedor("VEND001");
        vendaExistente.setFormaPagamento("DINHEIRO");
        vendaExistente.setItens(List.of(itemOriginal));

        ItemVenda itemNovo = new ItemVenda();
        itemNovo.setProduto(produto);
        itemNovo.setQuantidade(2);
        itemNovo.setValorUnitario(new BigDecimal("100.00"));

        Venda novosDados = new Venda();
        novosDados.setCliente(cliente);
        novosDados.setCodigoVendedor("VEND002");
        novosDados.setFormaPagamento("CARTAO");
        novosDados.setNumeroCartao("1234");
        novosDados.setItens(List.of(itemNovo));

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(vendaExistente));

        Mockito.when(repository.save(Mockito.any(Venda.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Venda resultado = useCase.executar(id, novosDados);

        assertEquals(id, resultado.getId());
        assertEquals("VEND002", resultado.getCodigoVendedor());
        assertEquals("CARTAO", resultado.getFormaPagamento());
        assertEquals("1234", resultado.getNumeroCartao());
        assertEquals(1, resultado.getItens().size());

        Mockito.verify(repository).findById(id);
        Mockito.verify(repository).save(vendaExistente);
    }
}
