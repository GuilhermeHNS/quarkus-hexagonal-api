package com.guilhermehns.adapters.in.controllers;

import com.guilhermehns.application.usecase.CriarClienteUseCase;
import com.guilhermehns.application.usecase.ListarClientesUseCase;
import com.guilhermehns.domain.model.Cliente;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/clientes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClienteResource {

    @Inject
    CriarClienteUseCase criarClienteUseCase;

    @Inject
    ListarClientesUseCase listarClientesUseCase;

    @GET
    public Response listar() {
        return Response.ok(listarClientesUseCase.executar()).build();
    }

    @POST
    public Response criar(Cliente cliente) {
        Cliente resultado = criarClienteUseCase.executar(cliente);
        return Response.status(Response.Status.CREATED)
                .entity(resultado)
                .build();
    }
}
