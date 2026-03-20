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

import static org.junit.jupiter.api.Assertions.*;

public class CriarVendaUseCaseTest {
    @Test
    void deveCriarVendaComSucesso() {
        VendaRepository repository = Mockito.mock(VendaRepository.class);

        CriarVendaUseCase useCase = new CriarVendaUseCase(repository);

        Cliente cliente = new Cliente();
        cliente.setId(UUID.randomUUID());

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
        assertNotNull(resultado.getId());
        assertEquals(cliente.getId(), resultado.getCliente().getId());
        assertEquals("VEND001", resultado.getCodigoVendedor());
        assertEquals("DINHEIRO", resultado.getFormaPagamento());
        assertEquals(1, resultado.getItens().size());
    }

    @Test
    void deveLancarExcecaoQuandoFormaPagamentoForNula() {
        VendaRepository repository = Mockito.mock(VendaRepository.class);
        CriarVendaUseCase useCase = new CriarVendaUseCase(repository);

        Venda venda = new Venda();
        venda.setCliente(new Cliente());
        venda.setCodigoVendedor("VEND001");
        venda.setDataVenda(LocalDateTime.now());
        venda.setFormaPagamento(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(venda)
        );

        assertEquals("Forma de pagamento é obrigatória.", exception.getMessage());
        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deveLancarExcecaoQuandoFormaPagamentoForInvalida() {
        VendaRepository repository = Mockito.mock(VendaRepository.class);
        CriarVendaUseCase useCase = new CriarVendaUseCase(repository);

        Venda venda = new Venda();
        venda.setCliente(new Cliente());
        venda.setCodigoVendedor("VEND001");
        venda.setDataVenda(LocalDateTime.now());
        venda.setFormaPagamento("PIX");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(venda)
        );

        assertEquals("Forma de pagamento inválida. Use DINHEIRO ou CARTAO_CREDITO.", exception.getMessage());
        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deveLancarExcecaoQuandoFormaPagamentoForCartaoCreditoESemNumeroCartao() {
        VendaRepository repository = Mockito.mock(VendaRepository.class);
        CriarVendaUseCase useCase = new CriarVendaUseCase(repository);

        Venda venda = new Venda();
        venda.setCliente(new Cliente());
        venda.setCodigoVendedor("VEND001");
        venda.setDataVenda(LocalDateTime.now());
        venda.setFormaPagamento("CARTAO_CREDITO");
        venda.setNumeroCartao(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(venda)
        );

        assertEquals("Número do cartão é obrigatório para pagamentos com cartão.", exception.getMessage());
        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deveLancarExcecaoQuandoFormaPagamentoForDinheiroESemValorPago() {
        VendaRepository repository = Mockito.mock(VendaRepository.class);
        CriarVendaUseCase useCase = new CriarVendaUseCase(repository);

        Venda venda = new Venda();
        venda.setCliente(new Cliente());
        venda.setCodigoVendedor("VEND001");
        venda.setDataVenda(LocalDateTime.now());
        venda.setFormaPagamento("DINHEIRO");
        venda.setValorPago(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(venda)
        );

        assertEquals("Valor pago é obrigatório para pagamentos em dinheiro.", exception.getMessage());
        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deveCriarVendaComSucessoQuandoFormaPagamentoForCartaoCreditoENumeroCartaoInformado() {
        VendaRepository repository = Mockito.mock(VendaRepository.class);
        CriarVendaUseCase useCase = new CriarVendaUseCase(repository);

        Cliente cliente = new Cliente();
        cliente.setId(UUID.randomUUID());

        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome("Produto Teste");

        ItemVenda item = new ItemVenda();
        item.setProduto(produto);
        item.setQuantidade(1);
        item.setValorUnitario(new BigDecimal("100.00"));

        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setCodigoVendedor("VEND001");
        venda.setDataVenda(LocalDateTime.now());
        venda.setFormaPagamento("CARTAO_CREDITO");
        venda.setNumeroCartao("4111111111111111");
        venda.setItens(List.of(item));

        Mockito.when(repository.save(Mockito.any(Venda.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Venda resultado = useCase.executar(venda);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("CARTAO_CREDITO", resultado.getFormaPagamento());
        assertEquals("4111111111111111", resultado.getNumeroCartao());
        Mockito.verify(repository).save(Mockito.any(Venda.class));
    }
}
