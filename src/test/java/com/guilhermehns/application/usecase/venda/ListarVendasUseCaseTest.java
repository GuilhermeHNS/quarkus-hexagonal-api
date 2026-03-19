package com.guilhermehns.application.usecase.venda;

import com.guilhermehns.domain.model.venda.Venda;
import com.guilhermehns.domain.repository.VendaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListarVendasUseCaseTest {
    @Test
    void deveListarVendasComSucesso() {
        VendaRepository repository = Mockito.mock(VendaRepository.class);

        ListarVendasUseCase useCase = new ListarVendasUseCase(repository);

        Venda venda1 = new Venda();
        venda1.setId(UUID.randomUUID());

        Venda venda2 = new Venda();
        venda2.setId(UUID.randomUUID());

        Mockito.when(repository.findAllVendas())
                .thenReturn(List.of(venda1, venda2));

        List<Venda> resultado = useCase.executar();

        assertEquals(2, resultado.size());
        Mockito.verify(repository).findAllVendas();
    }
}
