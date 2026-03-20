package com.guilhermehns.application.usecase.produto;

import com.guilhermehns.application.exception.ProdutoNaoEncontradoException;
import com.guilhermehns.domain.model.produto.Dimensoes;
import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class AtualizarProdutoUseCaseTest {

    private ProdutoRepository repository;
    private AtualizarProdutoUseCase useCase;

    @BeforeEach
    public void setUp() {
        repository = Mockito.mock(ProdutoRepository.class);
        useCase = new AtualizarProdutoUseCase(repository);
    }

    @Test
    void deveAtualizarProdutoComSucesso() {
        UUID produtoId = UUID.randomUUID();
        Produto produtoExistente = criarProdutoValido();
        Produto produtoAtualizado = criarProdutoValido();
        produtoAtualizado.setNome("Amortecedor Traseiro");

        Mockito.when(repository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));
        Mockito.when(repository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Produto resultado = useCase.executar(produtoId, produtoAtualizado);

        assertEquals("Amortecedor Traseiro", resultado.getNome());
        Mockito.verify(repository, Mockito.times(1)).findById(produtoId);
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExistirParaAtualizacao() {
        UUID produtoId = UUID.randomUUID();
        Produto produto = criarProdutoValido();

        Mockito.when(repository.findById(produtoId)).thenReturn(Optional.empty());

        ProdutoNaoEncontradoException excecao = assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> useCase.executar(produtoId, produto)
        );

        assertEquals("Produto não encontrado.", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(produtoId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoForNuloAoAtualizar() {
        UUID produtoId = UUID.randomUUID();
        Produto produtoExistente = criarProdutoValido();

        Mockito.when(repository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produtoId, null)
        );

        assertEquals("Produto é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(produtoId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNuloAoAtualizar() {
        UUID produtoId = UUID.randomUUID();
        Produto produtoExistente = criarProdutoValido();
        Produto produto = criarProdutoValido();
        produto.setNome(null);

        Mockito.when(repository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produtoId, produto)
        );

        assertEquals("Nome do produto é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(produtoId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoNomeForVazioAoAtualizar() {
        UUID produtoId = UUID.randomUUID();
        Produto produtoExistente = criarProdutoValido();
        Produto produto = criarProdutoValido();
        produto.setNome(" ");

        Mockito.when(repository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produtoId, produto)
        );

        assertEquals("Nome do produto é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(produtoId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoTipoForNuloAoAtualizar() {
        UUID produtoId = UUID.randomUUID();
        Produto produtoExistente = criarProdutoValido();
        Produto produto = criarProdutoValido();
        produto.setTipo(null);

        Mockito.when(repository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produtoId, produto)
        );

        assertEquals("Tipo do produto é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(produtoId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoTipoForVazioAoAtualizar() {
        UUID produtoId = UUID.randomUUID();
        Produto produtoExistente = criarProdutoValido();
        Produto produto = criarProdutoValido();
        produto.setTipo(" ");

        Mockito.when(repository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produtoId, produto)
        );

        assertEquals("Tipo do produto é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(produtoId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoDetalhesForNuloAoAtualizar() {
        UUID produtoId = UUID.randomUUID();
        Produto produtoExistente = criarProdutoValido();
        Produto produto = criarProdutoValido();
        produto.setDetalhes(null);

        Mockito.when(repository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produtoId, produto)
        );

        assertEquals("Detalhes do produto são obrigatórios", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(produtoId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoDetalhesForVazioAoAtualizar() {
        UUID produtoId = UUID.randomUUID();
        Produto produtoExistente = criarProdutoValido();
        Produto produto = criarProdutoValido();
        produto.setDetalhes(" ");

        Mockito.when(repository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produtoId, produto)
        );

        assertEquals("Detalhes do produto são obrigatórios", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(produtoId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoDimensoesForemNulasAoAtualizar() {
        UUID produtoId = UUID.randomUUID();
        Produto produtoExistente = criarProdutoValido();
        Produto produto = criarProdutoValido();
        produto.setDimensoes(null);

        Mockito.when(repository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produtoId, produto)
        );

        assertEquals("Dimensões do produto são obrigatórias", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(produtoId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoAlturaForNulaAoAtualizar() {
        UUID produtoId = UUID.randomUUID();
        Produto produtoExistente = criarProdutoValido();
        Produto produto = criarProdutoValido();
        produto.getDimensoes().setAltura(null);

        Mockito.when(repository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produtoId, produto)
        );

        assertEquals("Altura é obrigatória", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(produtoId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoLarguraForNulaAoAtualizar() {
        UUID produtoId = UUID.randomUUID();
        Produto produtoExistente = criarProdutoValido();
        Produto produto = criarProdutoValido();
        produto.getDimensoes().setLargura(null);

        Mockito.when(repository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produtoId, produto)
        );

        assertEquals("Largura é obrigatória", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(produtoId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoProfundidadeForNulaAoAtualizar() {
        UUID produtoId = UUID.randomUUID();
        Produto produtoExistente = criarProdutoValido();
        Produto produto = criarProdutoValido();
        produto.getDimensoes().setProfundidade(null);

        Mockito.when(repository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produtoId, produto)
        );

        assertEquals("Profundidade é obrigatória", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(produtoId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoPesoForNuloAoAtualizar() {
        UUID produtoId = UUID.randomUUID();
        Produto produtoExistente = criarProdutoValido();
        Produto produto = criarProdutoValido();
        produto.setPeso(null);

        Mockito.when(repository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produtoId, produto)
        );

        assertEquals("Peso é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(produtoId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoPrecoCompraForNuloAoAtualizar() {
        UUID produtoId = UUID.randomUUID();
        Produto produtoExistente = criarProdutoValido();
        Produto produto = criarProdutoValido();
        produto.setPrecoCompra(null);

        Mockito.when(repository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produtoId, produto)
        );

        assertEquals("Preço de compra é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(produtoId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoPrecoVendaForNuloAoAtualizar() {
        UUID produtoId = UUID.randomUUID();
        Produto produtoExistente = criarProdutoValido();
        Produto produto = criarProdutoValido();
        produto.setPrecoVenda(null);

        Mockito.when(repository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produtoId, produto)
        );

        assertEquals("Preço de venda é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(produtoId);
        verifyNoMoreInteractions(repository);
    }

    private Produto criarProdutoValido() {
        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome("Amortecedor Dianteiro");
        produto.setTipo("Amortecedor");
        produto.setDetalhes("Compatível com Gol G5");
        produto.setPeso(new BigDecimal("3.50"));
        produto.setPrecoCompra(new BigDecimal("120.00"));
        produto.setPrecoVenda(new BigDecimal("180.00"));
        produto.setDataCadastro(LocalDateTime.now());

        Dimensoes dimensoes = new Dimensoes();
        dimensoes.setAltura(new BigDecimal("10.00"));
        dimensoes.setLargura(new BigDecimal("20.00"));
        dimensoes.setProfundidade(new BigDecimal("30.00"));

        produto.setDimensoes(dimensoes);
        return produto;
    }
}

