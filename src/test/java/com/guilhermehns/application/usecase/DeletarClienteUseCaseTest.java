package com.guilhermehns.application.usecase;

import com.guilhermehns.domain.model.Cliente;
import com.guilhermehns.domain.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

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
}
