package com.guilhermehns.adapters.in.handlers;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class IllegalArgumentExceptionMapperTest {

    @Test
    void deveRetornarBadRequestComMensagemPadronizada() {
        IllegalArgumentExceptionMapper mapper = new IllegalArgumentExceptionMapper();
        IllegalArgumentException exception = new IllegalArgumentException("Campo obrigatório.");

        Response response = mapper.toResponse(exception);

        assertEquals(400, response.getStatus());

        assertInstanceOf(Map.class, response.getEntity());

        Map<?, ?> body = (Map<?, ?>) response.getEntity();
        assertEquals("Campo obrigatório.", body.get("mensagem"));
    }
}