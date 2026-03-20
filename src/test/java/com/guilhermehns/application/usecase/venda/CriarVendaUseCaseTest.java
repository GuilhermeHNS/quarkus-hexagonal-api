package com.guilhermehns.application.usecase.venda;

import com.guilhermehns.application.exception.ClienteNaoEncontradoException;
import com.guilhermehns.application.exception.ProdutoNaoEncontradoException;
import com.guilhermehns.domain.model.cliente.Cliente;
import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.model.venda.ItemVenda;
import com.guilhermehns.domain.model.venda.Venda;
import com.guilhermehns.domain.repository.ClienteRepository;
import com.guilhermehns.domain.repository.ProdutoRepository;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public class CriarVendaUseCaseTest {
    private VendaRepository repository;
    private ClienteRepository clienteRepository;
    private ProdutoRepository produtoRepository;
    private CriarVendaUseCase useCase;

    @BeforeEach
    public void mockaDependencias() {
        repository = mock(VendaRepository.class);
        clienteRepository = mock(ClienteRepository.class);
        produtoRepository = mock(ProdutoRepository.class);
        useCase = new CriarVendaUseCase(repository, clienteRepository, produtoRepository);
    }

    @Test
    void deveCriarVendaComSucesso() {
        Venda venda = criarVendaValida();
        UUID clienteId = venda.getCliente().getId();
        UUID produtoId = venda.getItens().getFirst().getProduto().getId();

        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(new Cliente()));

        Mockito.when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(new Produto()));

        Mockito.when(repository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Venda resultado = useCase.executar(venda);

        assertNotNull(resultado.getId());
        Mockito.verify(clienteRepository, Mockito.times(1)).findById(clienteId);
        Mockito.verify(produtoRepository, Mockito.times(1)).findById(produtoId);
        Mockito.verify(repository, Mockito.times(1)).save(venda);
        Mockito.verifyNoMoreInteractions(repository, clienteRepository, produtoRepository);
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExistir() {
        Venda venda = criarVendaValida();
        UUID clienteId = venda.getCliente().getId();

        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        ClienteNaoEncontradoException excecao = assertThrows(
                ClienteNaoEncontradoException.class,
                () -> useCase.executar(venda)
        );

        assertEquals("Cliente não encontrado.", excecao.getMessage());
        Mockito.verify(clienteRepository, Mockito.times(1)).findById(clienteId);
        Mockito.verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExistir() {
        Venda venda = criarVendaValida();
        UUID clienteId = venda.getCliente().getId();
        UUID produtoId = venda.getItens().getFirst().getProduto().getId();

        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(new Cliente()));

        Mockito.when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.empty());

        ProdutoNaoEncontradoException excecao = assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> useCase.executar(venda)
        );

        assertEquals("Produto não encontrado.", excecao.getMessage());
        Mockito.verify(clienteRepository, Mockito.times(1)).findById(clienteId);
        Mockito.verify(produtoRepository, Mockito.times(1)).findById(produtoId);
        Mockito.verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoVendaForNula() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(null)
        );

        assertEquals("Venda é obrigatória.", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoClienteForNulo() {
        Venda venda = criarVendaValida();
        venda.setCliente(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(venda)
        );

        assertEquals("Cliente é obrigatório.", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoCodigoVendedorForNulo() {
        Venda venda = criarVendaValida();
        venda.setCodigoVendedor(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(venda)
        );

        assertEquals("Código do vendedor é obrigatório.", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoCodigoVendedorForVazio() {
        Venda venda = criarVendaValida();
        venda.setCodigoVendedor(" ");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(venda)
        );

        assertEquals("Código do vendedor é obrigatório.", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoItensForemNulos() {
        Venda venda = criarVendaValida();
        venda.setItens(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(venda)
        );

        assertEquals("Itens da venda são obrigatórios.", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoItensForemVazios() {
        Venda venda = criarVendaValida();
        venda.setItens(List.of());

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(venda)
        );

        assertEquals("Itens da venda são obrigatórios.", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoDoItemForNulo() {
        Venda venda = criarVendaValida();
        venda.getItens().get(0).setProduto(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(venda)
        );

        assertEquals("Produto do item é obrigatório.", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoQuantidadeDoItemForNula() {
        Venda venda = criarVendaValida();
        venda.getItens().get(0).setQuantidade(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(venda)
        );

        assertEquals("Quantidade do item é obrigatória.", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoValorUnitarioDoItemForNulo() {
        Venda venda = criarVendaValida();
        venda.getItens().get(0).setValorUnitario(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(venda)
        );

        assertEquals("Valor unitário do item é obrigatório.", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoFormaPagamentoForNula() {
        Venda venda = criarVendaValida();
        venda.setFormaPagamento(null);

        UUID clienteId = venda.getCliente().getId();
        UUID produtoId = venda.getItens().get(0).getProduto().getId();

        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(venda.getCliente()));

        Mockito.when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(venda.getItens().get(0).getProduto()));


        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(venda)
        );

        assertEquals("Forma de pagamento é obrigatória.", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoFormaPagamentoForVazia() {
        Venda venda = criarVendaValida();
        venda.setFormaPagamento(" ");

        UUID clienteId = venda.getCliente().getId();
        UUID produtoId = venda.getItens().get(0).getProduto().getId();

        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(venda.getCliente()));

        Mockito.when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(venda.getItens().get(0).getProduto()));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(venda)
        );

        assertEquals("Forma de pagamento é obrigatória.", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoFormaPagamentoForInvalida() {
        Venda venda = criarVendaValida();
        venda.setFormaPagamento("PIX");

        UUID clienteId = venda.getCliente().getId();
        UUID produtoId = venda.getItens().get(0).getProduto().getId();

        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(venda.getCliente()));

        Mockito.when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(venda.getItens().get(0).getProduto()));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(venda)
        );

        assertEquals("Forma de pagamento inválida. Use DINHEIRO ou CARTAO_CREDITO.", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoNumeroCartaoForNuloParaCartao() {
        Venda venda = criarVendaValida();
        venda.setFormaPagamento("CARTAO_CREDITO");
        venda.setNumeroCartao(null);

        UUID clienteId = venda.getCliente().getId();
        UUID produtoId = venda.getItens().get(0).getProduto().getId();

        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(venda.getCliente()));

        Mockito.when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(venda.getItens().get(0).getProduto()));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(venda)
        );

        assertEquals("Número do cartão é obrigatório para pagamentos com cartão.", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoNumeroCartaoForVazioParaCartao() {
        Venda venda = criarVendaValida();
        venda.setFormaPagamento("CARTAO_CREDITO");
        venda.setNumeroCartao(" ");

        UUID clienteId = venda.getCliente().getId();
        UUID produtoId = venda.getItens().get(0).getProduto().getId();

        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(venda.getCliente()));

        Mockito.when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(venda.getItens().get(0).getProduto()));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(venda)
        );

        assertEquals("Número do cartão é obrigatório para pagamentos com cartão.", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoValorPagoForNuloParaDinheiro() {
        Venda venda = criarVendaValida();
        venda.setFormaPagamento("DINHEIRO");
        venda.setValorPago(null);

        UUID clienteId = venda.getCliente().getId();
        UUID produtoId = venda.getItens().get(0).getProduto().getId();

        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(venda.getCliente()));

        Mockito.when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(venda.getItens().get(0).getProduto()));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(venda)
        );

        assertEquals("Valor pago é obrigatório para pagamentos em dinheiro.", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    private Venda criarVendaValida() {
        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome("Notebook");

        Cliente cliente = new Cliente();
        cliente.setId(UUID.randomUUID());
        cliente.setNomeCompleto("João Silva");

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

        return venda;
    }

}
