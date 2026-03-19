package com.guilhermehns.adapters.in.controllers;

import com.guilhermehns.application.usecase.AtualizarClienteUseCase;
import com.guilhermehns.application.usecase.BuscarClientePorIdUseCase;
import com.guilhermehns.application.usecase.CriarClienteUseCase;
import com.guilhermehns.application.usecase.ListarClientesUseCase;
import com.guilhermehns.domain.model.Cliente;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/clientes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClienteResource {

    @Inject
    CriarClienteUseCase criarClienteUseCase;

    @Inject
    ListarClientesUseCase listarClientesUseCase;

    @Inject
    BuscarClientePorIdUseCase buscarClientePorIdUseCase;

    @Inject
    AtualizarClienteUseCase atualizarClienteUseCase;

    @GET
    public Response listar() {
        return Response.ok(listarClientesUseCase.executar()).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id")UUID id) {
        return Response.ok(buscarClientePorIdUseCase.executar(id)).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") UUID id, Cliente cliente) {
        Cliente clienteAtualizado = atualizarClienteUseCase.executar(id, cliente);
        return Response.ok(clienteAtualizado).build();
    }

    @POST
    public Response criar(Cliente cliente) {
        Cliente resultado = criarClienteUseCase.executar(cliente);
        return Response.status(Response.Status.CREATED)
                .entity(resultado)
                .build();
    }
}
