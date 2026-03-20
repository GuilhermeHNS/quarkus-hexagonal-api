package com.guilhermehns.adapters.in.handlers;

import com.guilhermehns.application.exception.VendaNaoEncontradaException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class VendaNaoEncontradaExceptionMapperTest {

    @Test
    void deveRetornarNotFoundComMensagemPadronizada() {
        VendaNaoEncontradaExceptionMapper mapper = new VendaNaoEncontradaExceptionMapper();
        VendaNaoEncontradaException exception = new VendaNaoEncontradaException();

        Response response = mapper.toResponse(exception);

        assertEquals(404, response.getStatus());

        assertInstanceOf(Map.class, response.getEntity());

        Map<?, ?> body = (Map<?, ?>) response.getEntity();
        assertEquals("Venda não encontrada.", body.get("mensagem"));
    }
}