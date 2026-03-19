package com.guilhermehns.adapters.in.controllers;

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
}
