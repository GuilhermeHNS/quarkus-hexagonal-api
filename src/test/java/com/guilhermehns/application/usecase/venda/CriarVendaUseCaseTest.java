package com.guilhermehns.application.usecase.venda;

import com.guilhermehns.domain.model.cliente.Cliente;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CriarVendaUseCaseTest {
    @Test
    void deveCriarVendaComSucesso() {
        VendaRepository repository = Mockito.mock(VendaRepository.class);

        CriarVendaUseCase useCase = new CriarVendaUseCase(repository);

        Cliente cliente = new Cliente();
        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome("Produto Teste");

        ItemVenda item = new ItemVenda();
        item.setProduto(produto);
        item.setQuantidade(2);
        item.setValorUnitario(new BigDecimal("100.00"));

        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setCodigoVendedor("VEND001");
        venda.setDataVenda(LocalDateTime.now());
        venda.setFormaPagamento("DINHEIRO");
        venda.setValorPago(new BigDecimal("200.00"));
        venda.setItens(List.of(item));

        Mockito.when(repository.save(Mockito.any(Venda.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Venda resultado = useCase.executar(venda);

        assertNotNull(resultado);
        assertEquals(cliente.getId(), resultado.getCliente().getId());
        assertEquals("VEND001", resultado.getCodigoVendedor());
        assertEquals("DINHEIRO", resultado.getFormaPagamento());
        assertEquals(1, resultado.getItens().size());
    }
}
