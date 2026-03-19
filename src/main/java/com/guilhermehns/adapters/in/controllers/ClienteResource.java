package com.guilhermehns.adapters.in.controllers;

import com.guilhermehns.application.usecase.CriarClienteUseCase;
import com.guilhermehns.domain.model.Cliente;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/clientes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClienteResource {

    @Inject
    CriarClienteUseCase criarClienteUseCase;

    @POST
    public Response criar(Cliente cliente) {
        Cliente resultado = criarClienteUseCase.executar(cliente);
        return Response.status(Response.Status.CREATED)
                .entity(resultado)
                .build();
    }
}
