package com.guilhermehns.adapters.in.controllers;

import com.guilhermehns.application.usecase.venda.*;
import com.guilhermehns.domain.model.venda.Venda;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/vendas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VendaResource {
    @Inject
    CriarVendaUseCase criarVendaUseCase;

    @Inject
    ListarVendasUseCase listarVendasUseCase;

    @Inject
    BuscarVendaPorIdUseCase buscarVendaPorIdUseCase;

    @Inject
    AtualizarVendaUseCase atualizarVendaUseCase;

    @Inject
    DeletarVendaUseCase deletarVendaUseCase;

    @POST
    public Response criar(Venda venda) {
        Venda vendaCriada = criarVendaUseCase.executar(venda);
        return Response.status(Response.Status.CREATED).entity(vendaCriada).build();
    }

    @GET
    public Response listar() {
        return Response.ok(listarVendasUseCase.executar()).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") UUID id) {
        return Response.ok(buscarVendaPorIdUseCase.executar(id)).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") UUID id, Venda venda){
        Venda vendaAtualizada = atualizarVendaUseCase.executar(id, venda);
        return Response.ok(vendaAtualizada).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") UUID id){
        deletarVendaUseCase.executar(id);
        return Response.noContent().build();
    }
}
