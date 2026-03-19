package com.guilhermehns.application.usecase.venda;

import com.guilhermehns.domain.model.venda.Venda;
import com.guilhermehns.domain.repository.VendaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BuscarVendaPorIdUseCaseTest {
    @Test
    void deveBuscarVendaPorIdComSucesso() {
        VendaRepository repository = Mockito.mock(VendaRepository.class);

        BuscarVendaPorIdUseCase useCase = new BuscarVendaPorIdUseCase(repository);

        UUID id = UUID.randomUUID();

        Venda venda = new Venda();
        venda.setId(id);

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(venda));

        Venda resultado = useCase.executar(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        Mockito.verify(repository).findById(id);
    }
}
