package com.guilhermehns.adapters.in.controllers;

import com.guilhermehns.application.usecase.venda.AtualizarVendaUseCase;
import com.guilhermehns.application.usecase.venda.BuscarVendaPorIdUseCase;
import com.guilhermehns.application.usecase.venda.CriarVendaUseCase;
import com.guilhermehns.application.usecase.venda.DeletarVendaUseCase;
import com.guilhermehns.application.usecase.venda.ListarVendasUseCase;
import com.guilhermehns.domain.model.cliente.Cliente;
import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.model.venda.ItemVenda;
import com.guilhermehns.domain.model.venda.Venda;
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
class VendaResourceTest {

    @InjectMock
    CriarVendaUseCase criarVendaUseCase;

    @InjectMock
    ListarVendasUseCase listarVendasUseCase;

    @InjectMock
    BuscarVendaPorIdUseCase buscarVendaPorIdUseCase;

    @InjectMock
    AtualizarVendaUseCase atualizarVendaUseCase;

    @InjectMock
    DeletarVendaUseCase deletarVendaUseCase;

    @Test
    void deveListarVendasComSucesso() {
        Venda venda = criarVendaValida();

        Mockito.when(listarVendasUseCase.executar())
                .thenReturn(List.of(venda));

        given()
                .when()
                .get("/vendas")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].codigoVendedor", equalTo("VEND001"))
                .body("[0].formaPagamento", equalTo("DINHEIRO"));
    }

    @Test
    void deveBuscarVendaPorIdComSucesso() {
        UUID vendaId = UUID.randomUUID();
        Venda venda = criarVendaValida();
        venda.setId(vendaId);

        Mockito.when(buscarVendaPorIdUseCase.executar(vendaId))
                .thenReturn(venda);

        given()
                .when()
                .get("/vendas/{id}", vendaId)
                .then()
                .statusCode(200)
                .body("id", equalTo(vendaId.toString()))
                .body("codigoVendedor", equalTo("VEND001"))
                .body("formaPagamento", equalTo("DINHEIRO"));
    }

    @Test
    void deveCriarVendaComSucesso() {
        Venda venda = criarVendaValida();
        Venda vendaCriada = criarVendaValida();
        UUID vendaId = UUID.randomUUID();
        vendaCriada.setId(vendaId);

        Mockito.when(criarVendaUseCase.executar(Mockito.any(Venda.class)))
                .thenReturn(vendaCriada);

        given()
                .contentType(ContentType.JSON)
                .body(venda)
                .when()
                .post("/vendas")
                .then()
                .statusCode(201)
                .body("id", equalTo(vendaId.toString()))
                .body("codigoVendedor", equalTo("VEND001"));
    }

    @Test
    void deveAtualizarVendaComSucesso() {
        UUID vendaId = UUID.randomUUID();
        Venda venda = criarVendaValida();
        Venda vendaAtualizada = criarVendaValida();
        vendaAtualizada.setId(vendaId);
        vendaAtualizada.setCodigoVendedor("VEND999");

        Mockito.when(atualizarVendaUseCase.executar(Mockito.eq(vendaId), Mockito.any(Venda.class)))
                .thenReturn(vendaAtualizada);

        given()
                .contentType(ContentType.JSON)
                .body(venda)
                .when()
                .put("/vendas/{id}", vendaId)
                .then()
                .statusCode(200)
                .body("id", equalTo(vendaId.toString()))
                .body("codigoVendedor", equalTo("VEND999"));
    }

    @Test
    void deveDeletarVendaComSucesso() {
        UUID vendaId = UUID.randomUUID();

        Mockito.doNothing().when(deletarVendaUseCase).executar(vendaId);

        given()
                .when()
                .delete("/vendas/{id}", vendaId)
                .then()
                .statusCode(204);
    }

    private Venda criarVendaValida() {
        Cliente cliente = new Cliente();
        cliente.setId(UUID.randomUUID());
        cliente.setNomeCompleto("João Silva");

        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome("Notebook");

        ItemVenda item = new ItemVenda();
        item.setProduto(produto);
        item.setQuantidade(2);
        item.setValorUnitario(new BigDecimal("100.00"));

        Venda venda = new Venda();
        venda.setId(UUID.randomUUID());
        venda.setCliente(cliente);
        venda.setCodigoVendedor("VEND001");
        venda.setDataVenda(LocalDateTime.of(2026, 3, 20, 10, 0));
        venda.setFormaPagamento("DINHEIRO");
        venda.setValorPago(new BigDecimal("200.00"));
        venda.setItens(List.of(item));

        return venda;
    }
}