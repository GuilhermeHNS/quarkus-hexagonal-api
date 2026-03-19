package com.guilhermehns.application.usecase.venda;

import com.guilhermehns.domain.model.venda.Venda;
import com.guilhermehns.domain.repository.VendaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

public class DeletarVendaUseCaseTest {
    @Test
    void deveDeletarVendaComSucesso() {
        VendaRepository repository = Mockito.mock(VendaRepository.class);

        DeletarVendaUseCase useCase = new DeletarVendaUseCase(repository);

        UUID id = UUID.randomUUID();

        Venda venda = new Venda();
        venda.setId(id);

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(venda));

        useCase.executar(id);

        Mockito.verify(repository).deleteById(id);
    }
}
