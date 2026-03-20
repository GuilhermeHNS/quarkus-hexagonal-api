package com.guilhermehns.application.usecase.relatorio;

import com.guilhermehns.application.dto.NovoClienteDTO;
import com.guilhermehns.domain.model.cliente.Cliente;
import com.guilhermehns.domain.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RelatorioNovosClientesUseCaseTest {
    @Test
    void deveRetornarNovosClientesDoAnoInformado() {
        ClienteRepository clienteRepository = Mockito.mock(ClienteRepository.class);
        RelatorioNovosClientesUseCase useCase = new RelatorioNovosClientesUseCase(clienteRepository);

        NovoClienteDTO dto = new NovoClienteDTO();
        dto.setClienteId(UUID.randomUUID());
        dto.setNomeCompleto("Ana Paula Ribeiro");
        dto.setDataNascimento(LocalDate.of(1995, 4, 12));
        dto.setDataCadastro(LocalDateTime.of(2026, 3, 20, 7, 19, 52));

        List<NovoClienteDTO> retornoEsperado = List.of(dto);

        Mockito.when(clienteRepository.buscarNovosClientesPorAno(2026))
                .thenReturn(retornoEsperado);

        List<NovoClienteDTO> resultado = useCase.executar(2026);

        assertEquals(1, resultado.size());
        assertEquals("Ana Paula Ribeiro", resultado.get(0).getNomeCompleto());
        assertEquals(LocalDate.of(1995, 4, 12), resultado.get(0).getDataNascimento());
        assertEquals(LocalDateTime.of(2026, 3, 20, 7, 19, 52), resultado.get(0).getDataCadastro());

        Mockito.verify(clienteRepository).buscarNovosClientesPorAno(2026);
    }
}
