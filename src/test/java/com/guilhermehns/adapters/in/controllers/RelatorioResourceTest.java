package com.guilhermehns.adapters.in.controllers;

import com.guilhermehns.application.dto.*;
import com.guilhermehns.application.usecase.relatorio.RelatorioEncalhadosUseCase;
import com.guilhermehns.application.usecase.relatorio.RelatorioFaturamentoMensalUseCase;
import com.guilhermehns.application.usecase.relatorio.RelatorioMaiorFaturamentoUseCase;
import com.guilhermehns.application.usecase.relatorio.RelatorioNovosClientesUseCase;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
class RelatorioSourceTest {

    @InjectMock
    RelatorioMaiorFaturamentoUseCase relatorioMaiorFaturamentoUseCase;

    @InjectMock
    RelatorioNovosClientesUseCase relatorioNovosClientesUseCase;

    @InjectMock
    RelatorioFaturamentoMensalUseCase relatorioFaturamentoMensalUseCase;

    @InjectMock
    RelatorioEncalhadosUseCase relatorioEncalhadosUseCase;

    @Test
    void deveRetornarRelatorioMaiorFaturamentoComSucesso() {
        ItemMaiorFaturamentoDTO item = new ItemMaiorFaturamentoDTO();
        item.setProdutoId(UUID.randomUUID());
        item.setNomeProduto("Notebook Dell");
        item.setPrecoVenda(new BigDecimal("3500.00"));

        Mockito.when(relatorioMaiorFaturamentoUseCase.executar())
                .thenReturn(List.of(item));

        given()
                .when()
                .get("/relatorios/maior-faturamento")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].nomeProduto", equalTo("Notebook Dell"))
                .body("[0].precoVenda", equalTo(3500.00f));
    }

    @Test
    void deveRetornarRelatorioNovosClientesComSucesso() {
        NovoClienteDTO dto = new NovoClienteDTO();
        dto.setClienteId(UUID.randomUUID());
        dto.setNomeCompleto("João Silva");
        dto.setDataNascimento(LocalDate.of(2000, 1, 1));
        dto.setDataCadastro(LocalDateTime.of(2025, 3, 20, 10, 0));

        Mockito.when(relatorioNovosClientesUseCase.executar(2025))
                .thenReturn(List.of(dto));

        given()
                .when()
                .get("/relatorios/novos-clientes/{ano}", 2025)
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].nomeCompleto", equalTo("João Silva"))
                .body("[0].dataNascimento", equalTo("2000-01-01"));
    }

    @Test
    void deveRetornarRelatorioFaturamentoMensalComSucesso() {
        FaturamentoMensalItemDTO item = new FaturamentoMensalItemDTO();
        item.setAno(2026);
        item.setMes(3);
        item.setFaturamento(new BigDecimal("100.00"));
        item.setImposto(new BigDecimal("9.00"));

        ResumoFaturamentoMensalDTO resumo = new ResumoFaturamentoMensalDTO();
        resumo.setFaturamentoLoja(new BigDecimal("100.00"));
        resumo.setImpostoTotal(new BigDecimal("9.00"));
        resumo.setFaturamentoMaisImposto(new BigDecimal("109.00"));

        RelatorioFaturamentoMensalDTO relatorio = new RelatorioFaturamentoMensalDTO();
        relatorio.setFaturamentosMensais(List.of(item));
        relatorio.setResumo(resumo);

        Mockito.when(relatorioFaturamentoMensalUseCase.executar("2026-03-20"))
                .thenReturn(relatorio);

        given()
                .when()
                .get("/relatorios/faturamento-mensal/{dataReferencia}", "2026-03-20")
                .then()
                .statusCode(200)
                .body("faturamentosMensais", hasSize(1))
                .body("faturamentosMensais[0].ano", equalTo(2026))
                .body("faturamentosMensais[0].mes", equalTo(3))
                .body("resumo.faturamentoLoja", equalTo(100.00f))
                .body("resumo.impostoTotal", equalTo(9.00f))
                .body("resumo.faturamentoMaisImposto", equalTo(109.00f));
    }

    @Test
    void deveRetornarRelatorioEncalhadosComSucesso() {
        EncalhadoDTO dto = new EncalhadoDTO();
        dto.setProdutoId(UUID.randomUUID());
        dto.setNomeProduto("Mouse Logitech");
        dto.setPeso(new BigDecimal("0.20"));
        dto.setPrecoCompra(new BigDecimal("45.00"));
        dto.setDataCadastro(LocalDateTime.of(2024, 1, 10, 10, 0));

        Mockito.when(relatorioEncalhadosUseCase.executar())
                .thenReturn(List.of(dto));

        given()
                .when()
                .get("/relatorios/encalhados")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].nomeProduto", equalTo("Mouse Logitech"))
                .body("[0].peso", equalTo(0.20f))
                .body("[0].precoCompra", equalTo(45.00f));
    }
}