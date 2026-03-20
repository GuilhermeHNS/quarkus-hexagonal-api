package com.guilhermehns.application.usecase.cliente;

import com.guilhermehns.application.exception.ClienteNaoEncontradoException;
import com.guilhermehns.domain.model.cliente.Cliente;
import com.guilhermehns.domain.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void deveLancarExcecaoQuandoClienteNaoForEncontradoAoBuscarPorId() {
        ClienteRepository repository = Mockito.mock(ClienteRepository.class);
        BuscarClientePorIdUseCase useCase = new BuscarClientePorIdUseCase(repository);

        UUID id = UUID.randomUUID();

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.empty());

        ClienteNaoEncontradoException exception = assertThrows(
                ClienteNaoEncontradoException.class,
                () -> useCase.executar(id)
        );

        assertEquals("Cliente não encontrado.", exception.getMessage());
        Mockito.verify(repository).findById(id);
    }
}
