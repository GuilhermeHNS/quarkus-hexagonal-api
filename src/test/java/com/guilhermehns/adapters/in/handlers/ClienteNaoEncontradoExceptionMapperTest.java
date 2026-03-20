package com.guilhermehns.adapters.in.handlers;

import com.guilhermehns.application.exception.ClienteNaoEncontradoException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class ClienteNaoEncontradoExceptionMapperTest {

    @Test
    void deveRetornarNotFoundComMensagemPadronizada() {
        ClienteNaoEncontradoExceptionMapper mapper = new ClienteNaoEncontradoExceptionMapper();
        ClienteNaoEncontradoException exception = new ClienteNaoEncontradoException();

        Response response = mapper.toResponse(exception);

        assertEquals(404, response.getStatus());

        assertInstanceOf(Map.class, response.getEntity());

        Map<?, ?> body = (Map<?, ?>) response.getEntity();
        assertEquals("Cliente não encontrado.", body.get("mensagem"));
    }
}