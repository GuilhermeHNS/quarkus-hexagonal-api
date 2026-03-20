package com.guilhermehns.adapters.in.handlers;

import com.guilhermehns.application.exception.ClienteNaoEncontradoException;
import com.guilhermehns.application.exception.ProdutoNaoEncontradoException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class ClienteNaoEncontradoExceptionMapper implements ExceptionMapper<ClienteNaoEncontradoException> {
    @Override
    public Response toResponse(ClienteNaoEncontradoException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of("mensagem", e.getMessage()))
                .build();
    }
}
