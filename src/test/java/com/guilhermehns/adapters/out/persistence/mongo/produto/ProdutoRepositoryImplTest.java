package com.guilhermehns.adapters.out.persistence.mongo.produto;

import com.guilhermehns.application.dto.EncalhadoDTO;
import com.guilhermehns.domain.model.produto.Dimensoes;
import com.guilhermehns.domain.model.produto.Produto;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ProdutoRepositoryImplTest {

    @Inject
    ProdutoRepositoryImpl produtoRepository;

    @BeforeEach
    void setUp() {
        produtoRepository.deleteAll();
    }

    @Test
    void deveSalvarProdutoComSucesso() {
        Produto produto = criarProdutoValido();

        Produto resultado = produtoRepository.save(produto);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals(produto.getNome(), resultado.getNome());
        assertEquals(produto.getTipo(), resultado.getTipo());
        assertEquals(produto.getDetalhes(), resultado.getDetalhes());
        assertEquals(produto.getPeso(), resultado.getPeso());
        assertEquals(produto.getPrecoCompra(), resultado.getPrecoCompra());
        assertEquals(produto.getPrecoVenda(), resultado.getPrecoVenda());
        assertNotNull(resultado.getDimensoes());
        assertEquals(produto.getDimensoes().getAltura(), resultado.getDimensoes().getAltura());
    }

    @Test
    void deveBuscarProdutoPorIdComSucesso() {
        Produto produto = criarProdutoValido();
        produtoRepository.save(produto);

        Optional<Produto> resultado = produtoRepository.findById(produto.getId());

        assertTrue(resultado.isPresent());
        assertEquals(produto.getId(), resultado.get().getId());
        assertEquals(produto.getNome(), resultado.get().getNome());
        assertEquals(produto.getPrecoVenda(), resultado.get().getPrecoVenda());
    }

    @Test
    void deveRetornarVazioQuandoProdutoNaoExistir() {
        Optional<Produto> resultado = produtoRepository.findById(UUID.randomUUID());

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveListarTodosOsProdutosOrdenadosPorNome() {
        Produto produto1 = criarProdutoValido();
        produto1.setNome("Notebook");

        Produto produto2 = criarProdutoValido();
        produto2.setId(UUID.randomUUID());
        produto2.setNome("Mouse");
        produto2.setTipo("Periférico");
        produto2.setDetalhes("Mouse sem fio");
        produto2.setPrecoCompra(new BigDecimal("50.00"));
        produto2.setPrecoVenda(new BigDecimal("90.00"));

        produtoRepository.save(produto1);
        produtoRepository.save(produto2);

        List<Produto> resultado = produtoRepository.findAllProdutos();

        assertEquals(2, resultado.size());
        assertEquals("Mouse", resultado.get(0).getNome());
        assertEquals("Notebook", resultado.get(1).getNome());
    }

    @Test
    void deveDeletarProdutoComSucesso() {
        Produto produto = criarProdutoValido();
        produtoRepository.save(produto);

        produtoRepository.deleteById(produto.getId());

        Optional<Produto> resultado = produtoRepository.findById(produto.getId());

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveBuscarProdutosEncalhadosComSucesso() {
        Produto produto1 = criarProdutoValido();
        produto1.setNome("Produto 1");
        produto1.setPrecoCompra(new BigDecimal("100.00"));
        produto1.setDataCadastro(LocalDateTime.of(2024, 1, 1, 10, 0));

        Produto produto2 = criarProdutoValido();
        produto2.setId(UUID.randomUUID());
        produto2.setNome("Produto 2");
        produto2.setPrecoCompra(new BigDecimal("300.00"));
        produto2.setDataCadastro(LocalDateTime.of(2024, 1, 2, 10, 0));

        Produto produto3 = criarProdutoValido();
        produto3.setId(UUID.randomUUID());
        produto3.setNome("Produto 3");
        produto3.setPrecoCompra(new BigDecimal("200.00"));
        produto3.setDataCadastro(LocalDateTime.of(2024, 1, 3, 10, 0));

        Produto produto4 = criarProdutoValido();
        produto4.setId(UUID.randomUUID());
        produto4.setNome("Produto 4");
        produto4.setPrecoCompra(new BigDecimal("5000.00"));
        produto4.setDataCadastro(LocalDateTime.of(2024, 1, 4, 10, 0));

        produtoRepository.save(produto1);
        produtoRepository.save(produto2);
        produtoRepository.save(produto3);
        produtoRepository.save(produto4);

        List<EncalhadoDTO> resultado = produtoRepository.buscarProdutosEncalhados();

        assertEquals(3, resultado.size());

        assertEquals("Produto 2", resultado.get(0).getNomeProduto());
        assertEquals(new BigDecimal("300.00"), resultado.get(0).getPrecoCompra());

        assertEquals("Produto 3", resultado.get(1).getNomeProduto());
        assertEquals(new BigDecimal("200.00"), resultado.get(1).getPrecoCompra());

        assertEquals("Produto 1", resultado.get(2).getNomeProduto());
        assertEquals(new BigDecimal("100.00"), resultado.get(2).getPrecoCompra());
    }

    private Produto criarProdutoValido() {
        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome("Notebook Dell");
        produto.setTipo("Eletrônico");
        produto.setDetalhes("Notebook i5 16GB RAM");
        produto.setPeso(new BigDecimal("2.50"));
        produto.setPrecoCompra(new BigDecimal("2500.00"));
        produto.setPrecoVenda(new BigDecimal("3500.00"));
        produto.setDataCadastro(LocalDateTime.of(2026, 3, 20, 10, 0));

        Dimensoes dimensoes = new Dimensoes();
        dimensoes.setAltura(new BigDecimal("2.00"));
        dimensoes.setLargura(new BigDecimal("35.00"));
        dimensoes.setProfundidade(new BigDecimal("25.00"));

        produto.setDimensoes(dimensoes);
        return produto;
    }
}