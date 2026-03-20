package com.guilhermehns.application.usecase.cliente;

import com.guilhermehns.application.exception.ClienteNaoEncontradoException;
import com.guilhermehns.domain.model.cliente.Cliente;
import com.guilhermehns.domain.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeletarClienteUseCaseTest {

    @Test
    void deveDeletarClienteComSucesso(){
        ClienteRepository repository = Mockito.mock(ClienteRepository.class);

        DeletarClienteUseCase useCase = new DeletarClienteUseCase(repository);

        UUID id = UUID.randomUUID();

        Cliente cliente = new Cliente();
        cliente.setId(id);

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(cliente));

        useCase.executar(id);

        Mockito.verify(repository).deleteById(id);
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoForEncontradoAoDeletar() {
        ClienteRepository repository = Mockito.mock(ClienteRepository.class);
        DeletarClienteUseCase useCase = new DeletarClienteUseCase(repository);

        UUID id = UUID.randomUUID();

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.empty());

        ClienteNaoEncontradoException exception = assertThrows(
                ClienteNaoEncontradoException.class,
                () -> useCase.executar(id)
        );

        assertEquals("Cliente não encontrado.", exception.getMessage());
        Mockito.verify(repository).findById(id);
        Mockito.verify(repository, Mockito.never()).deleteById(Mockito.any());
    }
}
