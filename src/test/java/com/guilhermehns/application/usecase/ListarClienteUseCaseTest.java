package com.guilhermehns.application.usecase;

import com.guilhermehns.domain.model.Cliente;
import com.guilhermehns.domain.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListarClienteUseCaseTest {
    @Test
    void deveListarClientesComSucesso() {

        ClienteRepository repository = Mockito.mock(ClienteRepository.class);

        ListarClientesUseCase useCase = new ListarClientesUseCase(repository);

        Cliente cliente = new Cliente();
        cliente.setId(UUID.randomUUID());
        cliente.setNomeCompleto("João Silva");

        Mockito.when(repository.findAllClientes())
                .thenReturn(List.of(cliente));

        List<Cliente> resultado = useCase.executar();

        assertEquals(1, resultado.size());
        assertEquals("João Silva", resultado.get(0).getNomeCompleto());
    }
}
