package com.guilhermehns.application.usecase;

import com.guilhermehns.domain.model.Cliente;
import com.guilhermehns.domain.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BuscarClientePorIdUseCaseTest {

    @Test
    void deveBuscarClientePorIdComSucesso() {
        ClienteRepository clienteRepository = Mockito.mock(ClienteRepository.class);

        BuscarClientePorIdUseCase useCase = new BuscarClientePorIdUseCase(clienteRepository);

        UUID id = UUID.randomUUID();

        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNomeCompleto("João Silva");

        Mockito.when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        Cliente resultado = useCase.executar(id);
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
    }
}
