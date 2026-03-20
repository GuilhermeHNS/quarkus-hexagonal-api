package com.guilhermehns.adapters.in.controllers;

import com.guilhermehns.application.usecase.venda.*;
import com.guilhermehns.domain.model.venda.Venda;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.UUID;

@Path("/vendas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Vendas", description = "Endpoints para gerenciamento de vendas")
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
    @Operation(summary = "Criar venda", description = "Cadastra uma nova venda.")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Venda criada com sucesso"),
            @APIResponse(responseCode = "400", description = "Dados inválidos"),
            @APIResponse(responseCode = "404", description = "Cliente ou produto não encontrado"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response criar(Venda venda) {
        Venda vendaCriada = criarVendaUseCase.executar(venda);
        return Response.status(Response.Status.CREATED).entity(vendaCriada).build();
    }

    @GET
    @Operation(summary = "Listar vendas", description = "Retorna todas as vendas cadastradas.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Vendas listadas com sucesso"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response listar() {
        return Response.ok(listarVendasUseCase.executar()).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar venda por ID", description = "Retorna uma venda a partir do identificador informado.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Venda encontrada com sucesso"),
            @APIResponse(responseCode = "404", description = "Venda não encontrada"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response buscarPorId(@PathParam("id") UUID id) {
        return Response.ok(buscarVendaPorIdUseCase.executar(id)).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar venda", description = "Atualiza os dados de uma venda existente.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Venda atualizada com sucesso"),
            @APIResponse(responseCode = "400", description = "Dados inválidos"),
            @APIResponse(responseCode = "404", description = "Venda, cliente ou produto não encontrado"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response atualizar(@PathParam("id") UUID id, Venda venda){
        Venda vendaAtualizada = atualizarVendaUseCase.executar(id, venda);
        return Response.ok(vendaAtualizada).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Deletar venda", description = "Remove uma venda a partir do identificador informado.")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Venda removida com sucesso"),
            @APIResponse(responseCode = "404", description = "Venda não encontrada"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response deletar(@PathParam("id") UUID id){
        deletarVendaUseCase.executar(id);
        return Response.noContent().build();
    }
}
