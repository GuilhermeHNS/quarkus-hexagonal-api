package com.guilhermehns.adapters.in.controllers;

import com.guilhermehns.application.usecase.produto.*;
import com.guilhermehns.domain.model.produto.Produto;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.UUID;

@Path("/produtos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Produtos", description = "Endpoints para gerenciamento de produtos")
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
    @Operation(summary = "Listar produtos", description = "Retorna todos os produtos cadastrados.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Produtos listados com sucesso"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response listar() {
        return Response.ok(listarProdutosUseCase.executar()).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna um produto a partir do identificador informado.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Produto encontrado com sucesso"),
            @APIResponse(responseCode = "404", description = "Produto não encontrado"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response buscarPorId(@PathParam("id") UUID id) {
        return Response.ok(buscarProdutoPorIdUseCase.executar(id)).build();
    }

    @POST
    @Operation(summary = "Criar produto", description = "Cadastra um novo produto.")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @APIResponse(responseCode = "400", description = "Dados inválidos"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response criar(Produto produto) {
        Produto produtoCriado = criarProdutoUseCase.executar(produto);
        return Response.status(Response.Status.CREATED).entity(produtoCriado).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto existente.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @APIResponse(responseCode = "400", description = "Dados inválidos"),
            @APIResponse(responseCode = "404", description = "Produto não encontrado"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response atualizar(@PathParam("id") UUID id, Produto produto) {
        Produto produtoAtualizado = atualizarProdutoUseCase.executar(id, produto);
        return Response.ok(produtoAtualizado).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Deletar produto", description = "Remove um produto a partir do identificador informado.")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Produto removido com sucesso"),
            @APIResponse(responseCode = "404", description = "Produto não encontrado"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response deletar(@PathParam("id") UUID id) {
        deletarProdutoUseCase.executar(id);
        return Response.noContent().build();
    }

}
