package com.guilhermehns.adapters.in.handlers;

import com.guilhermehns.application.exception.ProdutoNaoEncontradoException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class ProdutoNaoEncontradoExceptionMapperTest {

    @Test
    void deveRetornarNotFoundComMensagemPadronizada() {
        ProdutoNaoEncontradoExceptionMapper mapper = new ProdutoNaoEncontradoExceptionMapper();
        ProdutoNaoEncontradoException exception = new ProdutoNaoEncontradoException();

        Response response = mapper.toResponse(exception);

        assertEquals(404, response.getStatus());

        assertInstanceOf(Map.class, response.getEntity());

        Map<?, ?> body = (Map<?, ?>) response.getEntity();
        assertEquals("Produto não encontrado.", body.get("mensagem"));
    }
}