package com.guilhermehns.application.usecase.relatorio;

import com.guilhermehns.application.dto.NovoClienteDTO;
import com.guilhermehns.domain.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RelatorioNovosClientesUseCaseTest {

    private ClienteRepository clienteRepository;
    private RelatorioNovosClientesUseCase useCase;

    @BeforeEach
    void mockaDependencias() {
        clienteRepository = Mockito.mock(ClienteRepository.class);
        useCase = new RelatorioNovosClientesUseCase(clienteRepository);
    }

    @Test
    void deveBuscarNovosClientesPorAnoComSucesso() {
        int ano = 2026;
        List<NovoClienteDTO> retornoEsperado = List.of(new NovoClienteDTO());

        Mockito.when(clienteRepository.buscarNovosClientesPorAno(ano))
                .thenReturn(retornoEsperado);

        List<NovoClienteDTO> resultado = useCase.executar(ano);

        assertEquals(retornoEsperado, resultado);
        Mockito.verify(clienteRepository, Mockito.times(1)).buscarNovosClientesPorAno(ano);
        Mockito.verifyNoMoreInteractions(clienteRepository);
    }

    @Test
    void deveLancarExcecaoQuandoAnoForMenorOuIgualAZero() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(0)
        );

        assertEquals("Ano informado é inválido.", excecao.getMessage());
        Mockito.verifyNoInteractions(clienteRepository);
    }

    @Test
    void deveLancarExcecaoQuandoAnoForFuturo() {
        int anoFuturo = LocalDate.now().getYear() + 1;

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(anoFuturo)
        );

        assertEquals("Ano informado é inválido.", excecao.getMessage());
        Mockito.verifyNoInteractions(clienteRepository);
    }
}
