package com.guilhermehns.application.usecase.produto;

import com.guilhermehns.domain.model.produto.Dimensoes;
import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;

public class CriarProdutoUseCaseTest {

    private ProdutoRepository repository;
    private CriarProdutoUseCase useCase;

    @BeforeEach
    public void setUp() {
        repository = Mockito.mock(ProdutoRepository.class);
        useCase = new CriarProdutoUseCase(repository);
    }

    @Test
    void deveCriarProdutoComSucesso() {
        Produto produto = criarProdutoValido();

        Mockito.when(repository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Produto resultado = useCase.executar(produto);

        assertNotNull(resultado.getId());
        assertEquals("Amortecedor Dianteiro", resultado.getNome());
        Mockito.verify(repository, Mockito.times(1)).save(produto);
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoForNulo() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(null)
        );

        assertEquals("Produto é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNulo() {
        Produto produto = criarProdutoValido();
        produto.setNome(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produto)
        );

        assertEquals("Nome do produto é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoNomeForVazio() {
        Produto produto = criarProdutoValido();
        produto.setNome(" ");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produto)
        );

        assertEquals("Nome do produto é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoTipoForNulo() {
        Produto produto = criarProdutoValido();
        produto.setTipo(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produto)
        );

        assertEquals("Tipo do produto é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoTipoForVazio() {
        Produto produto = criarProdutoValido();
        produto.setTipo(" ");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produto)
        );

        assertEquals("Tipo do produto é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoDetalhesForNulo() {
        Produto produto = criarProdutoValido();
        produto.setDetalhes(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produto)
        );

        assertEquals("Detalhes do produto são obrigatórios", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoDetalhesForVazio() {
        Produto produto = criarProdutoValido();
        produto.setDetalhes(" ");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produto)
        );

        assertEquals("Detalhes do produto são obrigatórios", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoDimensoesForemNulas() {
        Produto produto = criarProdutoValido();
        produto.setDimensoes(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produto)
        );

        assertEquals("Dimensões do produto são obrigatórias", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoAlturaForNula() {
        Produto produto = criarProdutoValido();
        produto.getDimensoes().setAltura(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produto)
        );

        assertEquals("Altura é obrigatória", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoLarguraForNula() {
        Produto produto = criarProdutoValido();
        produto.getDimensoes().setLargura(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produto)
        );

        assertEquals("Largura é obrigatória", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoProfundidadeForNula() {
        Produto produto = criarProdutoValido();
        produto.getDimensoes().setProfundidade(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produto)
        );

        assertEquals("Profundidade é obrigatória", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoPesoForNulo() {
        Produto produto = criarProdutoValido();
        produto.setPeso(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produto)
        );

        assertEquals("Peso é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoPrecoCompraForNulo() {
        Produto produto = criarProdutoValido();
        produto.setPrecoCompra(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produto)
        );

        assertEquals("Preço de compra é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoPrecoVendaForNulo() {
        Produto produto = criarProdutoValido();
        produto.setPrecoVenda(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produto)
        );

        assertEquals("Preço de venda é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
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
