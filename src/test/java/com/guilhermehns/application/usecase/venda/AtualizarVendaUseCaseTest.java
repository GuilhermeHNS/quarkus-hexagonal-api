package com.guilhermehns.application.usecase.venda;

import com.guilhermehns.application.exception.ClienteNaoEncontradoException;
import com.guilhermehns.application.exception.ProdutoNaoEncontradoException;
import com.guilhermehns.application.exception.VendaNaoEncontradaException;
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
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class AtualizarVendaUseCaseTest {

    private VendaRepository vendaRepository;
    private ClienteRepository clienteRepository;
    private ProdutoRepository produtoRepository;
    private AtualizarVendaUseCase useCase;

    @BeforeEach
    public void setUp() {
        vendaRepository = Mockito.mock(VendaRepository.class);
        clienteRepository = Mockito.mock(ClienteRepository.class);
        produtoRepository = Mockito.mock(ProdutoRepository.class);
        useCase = new AtualizarVendaUseCase(vendaRepository, clienteRepository, produtoRepository);
    }

    @Test
    void deveAtualizarVendaComSucesso() {
        UUID vendaId = UUID.randomUUID();
        Venda vendaExistente = criarVendaValida();
        Venda vendaAtualizada = criarVendaValida();
        vendaAtualizada.setCodigoVendedor("VEND999");

        UUID clienteId = vendaAtualizada.getCliente().getId();
        UUID produtoId = vendaAtualizada.getItens().getFirst().getProduto().getId();

        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(vendaAtualizada.getCliente()));
        Mockito.when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(vendaAtualizada.getItens().getFirst().getProduto()));
        Mockito.when(vendaRepository.findById(vendaId))
                .thenReturn(Optional.of(vendaExistente));
        Mockito.when(vendaRepository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Venda resultado = useCase.executar(vendaId, vendaAtualizada);

        assertEquals("VEND999", resultado.getCodigoVendedor());
        Mockito.verify(clienteRepository, Mockito.times(1)).findById(clienteId);
        Mockito.verify(produtoRepository, Mockito.times(1)).findById(produtoId);
        Mockito.verify(vendaRepository, Mockito.times(1)).findById(vendaId);
        Mockito.verify(vendaRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verifyNoMoreInteractions(vendaRepository, clienteRepository, produtoRepository);
    }

    @Test
    void deveLancarExcecaoQuandoVendaAtualizadaForNula() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(UUID.randomUUID(), null)
        );

        assertEquals("Venda é obrigatória.", excecao.getMessage());
        Mockito.verifyNoInteractions(vendaRepository, clienteRepository, produtoRepository);
    }

    @Test
    void deveLancarExcecaoQuandoVendaNaoExistirParaAtualizacao() {
        UUID vendaId = UUID.randomUUID();
        Venda venda = criarVendaValida();

        UUID clienteId = venda.getCliente().getId();
        UUID produtoId = venda.getItens().getFirst().getProduto().getId();

        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(venda.getCliente()));
        Mockito.when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(venda.getItens().getFirst().getProduto()));
        Mockito.when(vendaRepository.findById(vendaId))
                .thenReturn(Optional.empty());

        assertThrows(VendaNaoEncontradaException.class, () -> useCase.executar(vendaId, venda));

        Mockito.verify(clienteRepository, Mockito.times(1)).findById(clienteId);
        Mockito.verify(produtoRepository, Mockito.times(1)).findById(produtoId);
        Mockito.verify(vendaRepository, Mockito.times(1)).findById(vendaId);
        verifyNoMoreInteractions(vendaRepository, clienteRepository, produtoRepository);
    }

    @Test
    void deveLancarExcecaoQuandoClienteForNuloAoAtualizar() {
        Venda venda = criarVendaValida();
        venda.setCliente(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(UUID.randomUUID(), venda)
        );

        assertEquals("Cliente é obrigatório.", excecao.getMessage());
        Mockito.verifyNoInteractions(vendaRepository, clienteRepository, produtoRepository);
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExistirAoAtualizar() {
        Venda venda = criarVendaValida();
        UUID clienteId = venda.getCliente().getId();

        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        assertThrows(ClienteNaoEncontradoException.class, () -> useCase.executar(UUID.randomUUID(), venda));

        Mockito.verify(clienteRepository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(vendaRepository, clienteRepository, produtoRepository);
    }

    @Test
    void deveLancarExcecaoQuandoCodigoVendedorForNuloAoAtualizar() {
        Venda venda = criarVendaValida();
        venda.setCodigoVendedor(null);

        UUID clienteId = venda.getCliente().getId();
        UUID produtoId = venda.getItens().getFirst().getProduto().getId();

        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(venda.getCliente()));
        Mockito.when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(venda.getItens().getFirst().getProduto()));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(UUID.randomUUID(), venda)
        );

        assertEquals("Código do vendedor é obrigatório.", excecao.getMessage());
        Mockito.verifyNoInteractions(vendaRepository);
    }

    @Test
    void deveLancarExcecaoQuandoCodigoVendedorForVazioAoAtualizar() {
        Venda venda = criarVendaValida();
        venda.setCodigoVendedor(" ");

        UUID clienteId = venda.getCliente().getId();
        UUID produtoId = venda.getItens().getFirst().getProduto().getId();

        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(venda.getCliente()));
        Mockito.when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(venda.getItens().getFirst().getProduto()));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(UUID.randomUUID(), venda)
        );

        assertEquals("Código do vendedor é obrigatório.", excecao.getMessage());
        Mockito.verifyNoInteractions(vendaRepository);
    }

    @Test
    void deveLancarExcecaoQuandoItensForemNulosAoAtualizar() {
        Venda venda = criarVendaValida();
        venda.setItens(null);

        UUID clienteId = venda.getCliente().getId();
        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(venda.getCliente()));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(UUID.randomUUID(), venda)
        );

        assertEquals("Itens da venda são obrigatórios.", excecao.getMessage());
        Mockito.verifyNoInteractions(vendaRepository);
    }

    @Test
    void deveLancarExcecaoQuandoItensForemVaziosAoAtualizar() {
        Venda venda = criarVendaValida();
        venda.setItens(List.of());

        UUID clienteId = venda.getCliente().getId();
        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(venda.getCliente()));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(UUID.randomUUID(), venda)
        );

        assertEquals("Itens da venda são obrigatórios.", excecao.getMessage());
        Mockito.verifyNoInteractions(vendaRepository);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoDoItemForNuloAoAtualizar() {
        Venda venda = criarVendaValida();
        venda.getItens().getFirst().setProduto(null);

        UUID clienteId = venda.getCliente().getId();
        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(venda.getCliente()));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(UUID.randomUUID(), venda)
        );

        assertEquals("Produto do item é obrigatório.", excecao.getMessage());
        Mockito.verifyNoInteractions(vendaRepository);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExistirAoAtualizar() {
        Venda venda = criarVendaValida();
        UUID clienteId = venda.getCliente().getId();
        UUID produtoId = venda.getItens().getFirst().getProduto().getId();

        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(venda.getCliente()));
        Mockito.when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.empty());

        assertThrows(ProdutoNaoEncontradoException.class, () -> useCase.executar(UUID.randomUUID(), venda));

        Mockito.verify(clienteRepository, Mockito.times(1)).findById(clienteId);
        Mockito.verify(produtoRepository, Mockito.times(1)).findById(produtoId);
        verifyNoMoreInteractions(vendaRepository, clienteRepository, produtoRepository);
    }

    @Test
    void deveLancarExcecaoQuandoQuantidadeDoItemForNulaAoAtualizar() {
        Venda venda = criarVendaValida();
        venda.getItens().getFirst().setQuantidade(null);

        UUID clienteId = venda.getCliente().getId();
        UUID produtoId = venda.getItens().getFirst().getProduto().getId();

        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(venda.getCliente()));
        Mockito.when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(venda.getItens().getFirst().getProduto()));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(UUID.randomUUID(), venda)
        );

        assertEquals("Quantidade do item é obrigatória.", excecao.getMessage());
        Mockito.verifyNoInteractions(vendaRepository);
    }

    @Test
    void deveLancarExcecaoQuandoValorUnitarioDoItemForNuloAoAtualizar() {
        Venda venda = criarVendaValida();
        venda.getItens().getFirst().setValorUnitario(null);

        UUID clienteId = venda.getCliente().getId();
        UUID produtoId = venda.getItens().getFirst().getProduto().getId();

        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(venda.getCliente()));
        Mockito.when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(venda.getItens().getFirst().getProduto()));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(UUID.randomUUID(), venda)
        );

        assertEquals("Valor unitário do item é obrigatório.", excecao.getMessage());
        Mockito.verifyNoInteractions(vendaRepository);
    }

    @Test
    void deveLancarExcecaoQuandoFormaPagamentoForNulaAoAtualizar() {
        Venda venda = criarVendaValida();
        venda.setFormaPagamento(null);

        UUID clienteId = venda.getCliente().getId();
        UUID produtoId = venda.getItens().getFirst().getProduto().getId();

        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(venda.getCliente()));
        Mockito.when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(venda.getItens().getFirst().getProduto()));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(UUID.randomUUID(), venda)
        );

        assertEquals("Forma de pagamento é obrigatória.", excecao.getMessage());
        Mockito.verifyNoInteractions(vendaRepository);
    }

    @Test
    void deveLancarExcecaoQuandoFormaPagamentoForVaziaAoAtualizar() {
        Venda venda = criarVendaValida();
        venda.setFormaPagamento(" ");

        UUID clienteId = venda.getCliente().getId();
        UUID produtoId = venda.getItens().getFirst().getProduto().getId();

        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(venda.getCliente()));
        Mockito.when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(venda.getItens().getFirst().getProduto()));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(UUID.randomUUID(), venda)
        );

        assertEquals("Forma de pagamento é obrigatória.", excecao.getMessage());
        Mockito.verifyNoInteractions(vendaRepository);
    }

    @Test
    void deveLancarExcecaoQuandoFormaPagamentoForInvalidaAoAtualizar() {
        Venda venda = criarVendaValida();
        venda.setFormaPagamento("PIX");

        UUID clienteId = venda.getCliente().getId();
        UUID produtoId = venda.getItens().getFirst().getProduto().getId();

        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(venda.getCliente()));
        Mockito.when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(venda.getItens().getFirst().getProduto()));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(UUID.randomUUID(), venda)
        );

        assertEquals("Forma de pagamento inválida. Use DINHEIRO ou CARTAO_CREDITO.", excecao.getMessage());
        Mockito.verifyNoInteractions(vendaRepository);
    }

    @Test
    void deveLancarExcecaoQuandoNumeroCartaoForNuloParaCartaoAoAtualizar() {
        Venda venda = criarVendaValida();
        venda.setFormaPagamento("CARTAO_CREDITO");
        venda.setNumeroCartao(null);

        UUID clienteId = venda.getCliente().getId();
        UUID produtoId = venda.getItens().getFirst().getProduto().getId();

        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(venda.getCliente()));
        Mockito.when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(venda.getItens().getFirst().getProduto()));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(UUID.randomUUID(), venda)
        );

        assertEquals("Número do cartão é obrigatório para pagamentos com cartão.", excecao.getMessage());
        Mockito.verifyNoInteractions(vendaRepository);
    }

    @Test
    void deveLancarExcecaoQuandoNumeroCartaoForVazioParaCartaoAoAtualizar() {
        Venda venda = criarVendaValida();
        venda.setFormaPagamento("CARTAO_CREDITO");
        venda.setNumeroCartao(" ");

        UUID clienteId = venda.getCliente().getId();
        UUID produtoId = venda.getItens().getFirst().getProduto().getId();

        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(venda.getCliente()));
        Mockito.when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(venda.getItens().getFirst().getProduto()));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(UUID.randomUUID(), venda)
        );

        assertEquals("Número do cartão é obrigatório para pagamentos com cartão.", excecao.getMessage());
        Mockito.verifyNoInteractions(vendaRepository);
    }

    @Test
    void deveLancarExcecaoQuandoValorPagoForNuloParaDinheiroAoAtualizar() {
        Venda venda = criarVendaValida();
        venda.setFormaPagamento("DINHEIRO");
        venda.setValorPago(null);

        UUID clienteId = venda.getCliente().getId();
        UUID produtoId = venda.getItens().getFirst().getProduto().getId();

        Mockito.when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(venda.getCliente()));
        Mockito.when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(venda.getItens().getFirst().getProduto()));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(UUID.randomUUID(), venda)
        );

        assertEquals("Valor pago é obrigatório para pagamentos em dinheiro.", excecao.getMessage());
        Mockito.verifyNoInteractions(vendaRepository);
    }

    private Venda criarVendaValida() {
        Cliente cliente = new Cliente();
        cliente.setId(UUID.randomUUID());

        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome("Notebook");

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