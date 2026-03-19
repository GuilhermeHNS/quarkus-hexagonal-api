package com.guilhermehns.adapters.in.controllers;

import com.guilhermehns.application.usecase.produto.*;
import com.guilhermehns.domain.model.produto.Produto;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/produtos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProdutoResource {

    @Inject
    CriarProdutoUseCase criarProdutoUseCase;

    @Inject
    ListarProdutosUseCase listarProdutosUseCase;

    @Inject
    BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase;

    @Inject
    AtualizarProdutoUseCase atualizarProdutoUseCase;

    @Inject
    DeletarProdutoUseCase deletarProdutoUseCase;

    @GET
    public Response listar() {
        return Response.ok(listarProdutosUseCase.executar()).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") UUID id) {
        return Response.ok(buscarProdutoPorIdUseCase.executar(id)).build();
    }

    @POST
    public Response criar(Produto produto) {
        Produto produtoCriado = criarProdutoUseCase.executar(produto);
        return Response.status(Response.Status.CREATED).entity(produtoCriado).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") UUID id, Produto produto) {
        Produto produtoAtualizado = atualizarProdutoUseCase.executar(id, produto);
        return Response.ok(produtoAtualizado).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") UUID id) {
        deletarProdutoUseCase.executar(id);
        return Response.noContent().build();
    }

}
