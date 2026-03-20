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

@Path("/relatorios")
@Produces(MediaType.APPLICATION_JSON)
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
    public Response maiorFaturamento() {
        return Response.ok(relatorioMaiorFaturamentoUseCase.executar()).build();
    }

    @GET
    @Path("/novos-clientes/{ano}")
    public Response novosClientes(@PathParam("ano")int ano) {
        return Response.ok(relatorioNovosClientesUseCase.executar(ano)).build();
    }

    @GET
    @Path("/faturamento-mensal/{dataReferencia}")
    public Response faturamentoMensal(@PathParam("dataReferencia") String dataReferencia) {
        return Response.ok(relatorioFaturamentoMensalUseCase.executar(dataReferencia)).build();
    }

    @GET
    @Path("/encalhados")
    public Response encalhados() {
        return Response.ok(relatorioEncalhadosUseCase.executar()).build();
    }
}
