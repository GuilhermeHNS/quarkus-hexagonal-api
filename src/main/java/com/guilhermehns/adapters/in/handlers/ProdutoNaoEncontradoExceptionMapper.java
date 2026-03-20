package com.guilhermehns.adapters.in.handlers;

import com.guilhermehns.application.exception.ProdutoNaoEncontradoException;
import com.guilhermehns.application.exception.VendaNaoEncontradaException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class ProdutoNaoEncontradoExceptionMapper implements ExceptionMapper<ProdutoNaoEncontradoException> {
    @Override
    public Response toResponse(ProdutoNaoEncontradoException e) {
        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of("mensagem", e.getMessage()))
                .build();
    }
}
