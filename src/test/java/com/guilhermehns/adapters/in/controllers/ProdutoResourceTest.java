package com.guilhermehns.adapters.in.controllers;

import com.guilhermehns.application.usecase.produto.AtualizarProdutoUseCase;
import com.guilhermehns.application.usecase.produto.BuscarProdutoPorIdUseCase;
import com.guilhermehns.application.usecase.produto.CriarProdutoUseCase;
import com.guilhermehns.application.usecase.produto.DeletarProdutoUseCase;
import com.guilhermehns.application.usecase.produto.ListarProdutosUseCase;
import com.guilhermehns.domain.model.produto.Dimensoes;
import com.guilhermehns.domain.model.produto.Produto;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
class ProdutoResourceTest {

    @InjectMock
    CriarProdutoUseCase criarProdutoUseCase;

    @InjectMock
    ListarProdutosUseCase listarProdutosUseCase;

    @InjectMock
    BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase;

    @InjectMock
    AtualizarProdutoUseCase atualizarProdutoUseCase;

    @InjectMock
    DeletarProdutoUseCase deletarProdutoUseCase;

    @Test
    void deveListarProdutosComSucesso() {
        Produto produto = criarProdutoValido();

        Mockito.when(listarProdutosUseCase.executar())
                .thenReturn(List.of(produto));

        given()
                .when()
                .get("/produtos")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].nome", equalTo("Notebook Dell"))
                .body("[0].tipo", equalTo("Eletrônico"));
    }

    @Test
    void deveBuscarProdutoPorIdComSucesso() {
        UUID produtoId = UUID.randomUUID();
        Produto produto = criarProdutoValido();
        produto.setId(produtoId);

        Mockito.when(buscarProdutoPorIdUseCase.executar(produtoId))
                .thenReturn(produto);

        given()
                .when()
                .get("/produtos/{id}", produtoId)
                .then()
                .statusCode(200)
                .body("id", equalTo(produtoId.toString()))
                .body("nome", equalTo("Notebook Dell"))
                .body("tipo", equalTo("Eletrônico"));
    }

    @Test
    void deveCriarProdutoComSucesso() {
        Produto produto = criarProdutoValido();
        Produto produtoCriado = criarProdutoValido();
        UUID produtoId = UUID.randomUUID();
        produtoCriado.setId(produtoId);

        Mockito.when(criarProdutoUseCase.executar(Mockito.any(Produto.class)))
                .thenReturn(produtoCriado);

        given()
                .contentType(ContentType.JSON)
                .body(produto)
                .when()
                .post("/produtos")
                .then()
                .statusCode(201)
                .body("id", equalTo(produtoId.toString()))
                .body("nome", equalTo("Notebook Dell"));
    }

    @Test
    void deveAtualizarProdutoComSucesso() {
        UUID produtoId = UUID.randomUUID();
        Produto produto = criarProdutoValido();
        Produto produtoAtualizado = criarProdutoValido();
        produtoAtualizado.setId(produtoId);
        produtoAtualizado.setNome("Notebook Lenovo");

        Mockito.when(atualizarProdutoUseCase.executar(Mockito.eq(produtoId), Mockito.any(Produto.class)))
                .thenReturn(produtoAtualizado);

        given()
                .contentType(ContentType.JSON)
                .body(produto)
                .when()
                .put("/produtos/{id}", produtoId)
                .then()
                .statusCode(200)
                .body("id", equalTo(produtoId.toString()))
                .body("nome", equalTo("Notebook Lenovo"));
    }

    @Test
    void deveDeletarProdutoComSucesso() {
        UUID produtoId = UUID.randomUUID();

        Mockito.doNothing().when(deletarProdutoUseCase).executar(produtoId);

        given()
                .when()
                .delete("/produtos/{id}", produtoId)
                .then()
                .statusCode(204);
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