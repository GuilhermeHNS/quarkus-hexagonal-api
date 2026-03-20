package com.guilhermehns.adapters.in.controllers;

import com.guilhermehns.application.usecase.cliente.*;
import com.guilhermehns.domain.model.cliente.Cliente;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.UUID;

@Path("/clientes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Clientes", description = "Endpoints para gerenciamento de clientes")
public class ClienteResource {

    @Inject
    CriarClienteUseCase criarClienteUseCase;

    @Inject
    ListarClientesUseCase listarClientesUseCase;

    @Inject
    BuscarClientePorIdUseCase buscarClientePorIdUseCase;

    @Inject
    AtualizarClienteUseCase atualizarClienteUseCase;

    @Inject
    DeletarClienteUseCase deletarClienteUseCase;

    @GET
    @Operation(summary = "Listar clientes", description = "Retorna todos os clientes cadastrados.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Clientes listados com sucesso"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response listar() {
        return Response.ok(listarClientesUseCase.executar()).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Retorna um cliente a partir do identificador informado.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Cliente encontrado com sucesso"),
            @APIResponse(responseCode = "404", description = "Cliente não encontrado"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response buscarPorId(@PathParam("id")UUID id) {
        return Response.ok(buscarClientePorIdUseCase.executar(id)).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @APIResponse(responseCode = "400", description = "Dados inválidos"),
            @APIResponse(responseCode = "404", description = "Cliente não encontrado"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response atualizar(@PathParam("id") UUID id, Cliente cliente) {
        Cliente clienteAtualizado = atualizarClienteUseCase.executar(id, cliente);
        return Response.ok(clienteAtualizado).build();
    }

    @POST
    @Operation(summary = "Criar cliente", description = "Cadastra um novo cliente.")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @APIResponse(responseCode = "400", description = "Dados inválidos"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response criar(Cliente cliente) {
        Cliente resultado = criarClienteUseCase.executar(cliente);
        return Response.status(Response.Status.CREATED)
                .entity(resultado)
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Deletar cliente", description = "Remove um cliente a partir do identificador informado.")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Cliente removido com sucesso"),
            @APIResponse(responseCode = "404", description = "Cliente não encontrado"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response deletar(@PathParam("id") UUID id) {
        deletarClienteUseCase.executar(id);
        return Response.noContent().build();
    }
}
