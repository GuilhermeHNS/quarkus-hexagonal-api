package com.guilhermehns.adapters.in.controllers;

import com.guilhermehns.application.usecase.relatorio.RelatorioEncalhadosUseCase;
import com.guilhermehns.application.usecase.relatorio.RelatorioFaturamentoMensalUseCase;
import com.guilhermehns.application.usecase.relatorio.RelatorioMaiorFaturamentoUseCase;
import com.guilhermehns.application.usecase.relatorio.RelatorioNovosClientesUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/relatorios")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Relatórios", description = "Endpoints para geração dos relatórios do sistema")
public class RelatorioSource {
    @Inject
    RelatorioMaiorFaturamentoUseCase relatorioMaiorFaturamentoUseCase;

    @Inject
    RelatorioNovosClientesUseCase relatorioNovosClientesUseCase;

    @Inject
    RelatorioFaturamentoMensalUseCase relatorioFaturamentoMensalUseCase;

    @Inject
    RelatorioEncalhadosUseCase relatorioEncalhadosUseCase;

    @GET
    @Path("/maior-faturamento")
    @Operation(summary = "Relatório de maior faturamento", description = "Retorna os 4 itens que mais trouxeram faturamento para a loja.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response maiorFaturamento() {
        return Response.ok(relatorioMaiorFaturamentoUseCase.executar()).build();
    }

    @GET
    @Path("/novos-clientes/{ano}")
    @Operation(summary = "Relatório de novos clientes", description = "Retorna os clientes cadastrados no ano informado.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
            @APIResponse(responseCode = "400", description = "Ano informado é inválido"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response novosClientes(@PathParam("ano")int ano) {
        return Response.ok(relatorioNovosClientesUseCase.executar(ano)).build();
    }

    @GET
    @Path("/faturamento-mensal/{dataReferencia}")
    @Operation(summary = "Relatório de faturamento mensal", description = "Retorna o faturamento dos últimos 12 meses a partir da data informada, com resumo final do período.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
            @APIResponse(responseCode = "400", description = "Data de referência inválida"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response faturamentoMensal(@PathParam("dataReferencia") String dataReferencia) {
        return Response.ok(relatorioFaturamentoMensalUseCase.executar(dataReferencia)).build();
    }

    @GET
    @Path("/encalhados")
    @Operation(summary = "Relatório de encalhados", description = "Retorna os 3 produtos mais antigos cadastrados, ordenados do mais caro para o mais barato.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response encalhados() {
        return Response.ok(relatorioEncalhadosUseCase.executar()).build();
    }
}
