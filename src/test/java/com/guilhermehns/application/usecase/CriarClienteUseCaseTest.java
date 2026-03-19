package com.guilhermehns.application.usecase;

import com.guilhermehns.domain.model.Cliente;
import com.guilhermehns.domain.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CriarClienteUseCaseTest {

    @Test
    void deveCriarClienteComSucesso(){
        ClienteRepository repository =  Mockito.mock(ClienteRepository.class);

        CriarClienteUseCase useCase = new CriarClienteUseCase(repository);
        Cliente cliente = new Cliente();
        cliente.setNomeCompleto("João Silva");
        cliente.setCpf("12345678900");
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));

        Mockito.when(repository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Cliente resultado = useCase.executar(cliente);

        assertNotNull(resultado.getId());
        assertNotNull(resultado.getDataCadastro());
        assertEquals("João Silva", resultado.getNomeCompleto());

    }

    @Test
    void deveLancarErroQuandoNomeForNulo() {
        ClienteRepository repository = Mockito.mock(ClienteRepository.class);
        CriarClienteUseCase useCase = new CriarClienteUseCase(repository);

        Cliente cliente = new Cliente();
        cliente.setNomeCompleto(null);
        cliente.setCpf("12345678900");
        cliente.setDataNascimento(LocalDate.of(1990,1,1));

        assertThrows(RuntimeException.class, () -> {
            useCase.executar(cliente);
        });
    }

    @Test
    void deveLancarErroQuandoCpfForNulo() {
        ClienteRepository repository = Mockito.mock(ClienteRepository.class);
        CriarClienteUseCase useCase = new CriarClienteUseCase(repository);

        Cliente cliente = new Cliente();
        cliente.setNomeCompleto("João Silva");
        cliente.setCpf(null);
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));

        assertThrows(RuntimeException.class, () -> {
            useCase.executar(cliente);
        });
    }
}
