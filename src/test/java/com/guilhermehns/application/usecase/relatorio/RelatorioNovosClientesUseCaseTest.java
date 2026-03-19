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
    void deveRetornarClientesCadastradosNoAnoInformado() {
        ClienteRepository clienteRepository = Mockito.mock(ClienteRepository.class);

        RelatorioNovosClientesUseCase useCase = new RelatorioNovosClientesUseCase(clienteRepository);

        Cliente cliente1 = new Cliente();
        cliente1.setId(UUID.randomUUID());
        cliente1.setNomeCompleto("Ana Souza");
        cliente1.setDataNascimento(LocalDate.of(1995, 5, 10));
        cliente1.setDataCadastro(LocalDateTime.of(2025, 3, 1, 10, 0));

        Cliente cliente2 = new Cliente();
        cliente2.setId(UUID.randomUUID());
        cliente2.setNomeCompleto("Bruno Lima");
        cliente2.setDataNascimento(LocalDate.of(1990, 8, 20));
        cliente2.setDataCadastro(LocalDateTime.of(2025, 7, 15, 14, 30));

        Cliente cliente3 = new Cliente();
        cliente3.setId(UUID.randomUUID());
        cliente3.setNomeCompleto("Carlos Dias");
        cliente3.setDataNascimento(LocalDate.of(1988, 1, 5));
        cliente3.setDataCadastro(LocalDateTime.of(2024, 12, 31, 23, 59));

        Mockito.when(clienteRepository.findAllClientes())
                .thenReturn(List.of(cliente1, cliente2, cliente3));

        List<NovoClienteDTO> resultado = useCase.executar(2025);

        assertEquals(2, resultado.size());
        assertEquals(cliente1.getId(), resultado.get(0).getClienteId());
        assertEquals(cliente2.getId(), resultado.get(1).getClienteId());

        Mockito.verify(clienteRepository).findAllClientes();
    }
}
