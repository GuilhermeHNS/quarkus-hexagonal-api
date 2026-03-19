package com.guilhermehns.application.usecase.produto;

import com.guilhermehns.domain.model.produto.Dimensoes;
import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CriarProdutoUseCaseTest {

    @Test
    void deveCriarProdutoComSucesso() {
        ProdutoRepository repository = Mockito.mock(ProdutoRepository.class);

        CriarProdutoUseCase useCase = new CriarProdutoUseCase(repository);

        Produto produto = new Produto();
        produto.setNome("Notebook");
        produto.setTipo("Eletrônico");
        produto.setDetalhes("Notebook Dell i5");
        produto.setPeso(new BigDecimal("2.5"));
        produto.setPrecoCompra(new BigDecimal("2500"));
        produto.setPrecoVenda(new BigDecimal("3500"));

        Dimensoes dimensoes = new Dimensoes();
        dimensoes.setAltura(new BigDecimal("2"));
        dimensoes.setLargura(new BigDecimal("30"));
        dimensoes.setProfundidade(new BigDecimal("20"));

        produto.setDimensoes(dimensoes);

        Mockito.when(repository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Produto resultado = useCase.executar(produto);

        assertNotNull(resultado.getId());
        assertNotNull(resultado.getDataCadastro());
        assertEquals("Notebook", resultado.getNome());
        assertEquals("Eletrônico", resultado.getTipo());
        assertEquals(new BigDecimal("3500"), resultado.getPrecoVenda());
    }

    @Test
    void deveLancarErroQuandoNomeForNulo() {

        ProdutoRepository repository = Mockito.mock(ProdutoRepository.class);

        CriarProdutoUseCase useCase = new CriarProdutoUseCase(repository);

        Produto produto = new Produto();
        produto.setTipo("Eletrônico");
        produto.setPrecoCompra(new BigDecimal("100"));
        produto.setPrecoVenda(new BigDecimal("200"));

        assertThrows(RuntimeException.class, () -> {
            useCase.executar(produto);
        });
    }

    @Test
    void deveLancarErroQuandoTipoForNulo() {

        ProdutoRepository repository = Mockito.mock(ProdutoRepository.class);

        CriarProdutoUseCase useCase = new CriarProdutoUseCase(repository);

        Produto produto = new Produto();
        produto.setNome("Notebook");
        produto.setPrecoCompra(new BigDecimal("100"));
        produto.setPrecoVenda(new BigDecimal("200"));

        assertThrows(RuntimeException.class, () -> {
            useCase.executar(produto);
        });
    }

    @Test
    void deveLancarErroQuandoPrecoCompraForNulo() {

        ProdutoRepository repository = Mockito.mock(ProdutoRepository.class);

        CriarProdutoUseCase useCase = new CriarProdutoUseCase(repository);

        Produto produto = new Produto();
        produto.setNome("Notebook");
        produto.setTipo("Eletrônico");
        produto.setPrecoVenda(new BigDecimal("200"));

        assertThrows(RuntimeException.class, () -> {
            useCase.executar(produto);
        });
    }

    @Test
    void deveLancarErroQuandoPrecoVendaForNulo() {

        ProdutoRepository repository = Mockito.mock(ProdutoRepository.class);

        CriarProdutoUseCase useCase = new CriarProdutoUseCase(repository);

        Produto produto = new Produto();
        produto.setNome("Notebook");
        produto.setTipo("Eletrônico");
        produto.setPrecoCompra(new BigDecimal("100"));

        assertThrows(RuntimeException.class, () -> {
            useCase.executar(produto);
        });
    }
}
