package com.guilhermehns.application.util;

import com.guilhermehns.domain.model.produto.Dimensoes;
import com.guilhermehns.domain.model.produto.Produto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProdutoValidationHelperTest {

    @Test
    void deveValidarProdutoComSucesso() {
        assertDoesNotThrow(() -> ProdutoValidationHelper.validarProduto(criarProdutoValido()));
    }

    @Test
    void deveLancarExcecaoQuandoProdutoForNulo() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> ProdutoValidationHelper.validarProduto(null)
        );

        assertEquals("Produto é obrigatório", excecao.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoDimensoesForemNulas() {
        Produto produto = criarProdutoValido();
        produto.setDimensoes(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> ProdutoValidationHelper.validarProduto(produto)
        );

        assertEquals("Dimensões do produto são obrigatórias", excecao.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoAlturaForNula() {
        Produto produto = criarProdutoValido();
        produto.getDimensoes().setAltura(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> ProdutoValidationHelper.validarProduto(produto)
        );

        assertEquals("Altura é obrigatória", excecao.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoCampoObrigatorioForVazio() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> ProdutoValidationHelper.validarCampoObrigatorio(" ", "Campo obrigatório")
        );

        assertEquals("Campo obrigatório", excecao.getMessage());
    }

    private Produto criarProdutoValido() {
        Produto produto = new Produto();
        produto.setNome("Notebook");
        produto.setTipo("Eletrônico");
        produto.setDetalhes("Teste");
        produto.setPeso(new BigDecimal("2.50"));
        produto.setPrecoCompra(new BigDecimal("2500.00"));
        produto.setPrecoVenda(new BigDecimal("3500.00"));

        Dimensoes dimensoes = new Dimensoes();
        dimensoes.setAltura(new BigDecimal("2.00"));
        dimensoes.setLargura(new BigDecimal("35.00"));
        dimensoes.setProfundidade(new BigDecimal("25.00"));

        produto.setDimensoes(dimensoes);
        return produto;
    }
}