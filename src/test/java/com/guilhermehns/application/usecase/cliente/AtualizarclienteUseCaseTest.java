package com.guilhermehns.application.usecase.cliente;

import com.guilhermehns.application.exception.ClienteNaoEncontradoException;
import com.guilhermehns.domain.model.cliente.Cliente;
import com.guilhermehns.domain.model.cliente.Endereco;
import com.guilhermehns.domain.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AtualizarclienteUseCaseTest {

    @Test
    void deveAtualizarTodosOsCamposDoClienteComSucesso() {
        ClienteRepository repository = Mockito.mock(ClienteRepository.class);

        AtualizarClienteUseCase useCase = new AtualizarClienteUseCase(repository);

        UUID id = UUID.randomUUID();

        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(id);
        clienteExistente.setNomeCompleto("João Silva");
        clienteExistente.setNomeMae("Maria Silva");
        clienteExistente.setCpf("12345678900");
        clienteExistente.setRg("11111111");
        clienteExistente.setEmail("joao@email.com");
        clienteExistente.setTelefone("19999999999");
        clienteExistente.setDataNascimento(LocalDate.of(1990, 1, 1));
        clienteExistente.setDataCadastro(LocalDateTime.now());

        Endereco enderecoAntigo = new Endereco();
        enderecoAntigo.setCep("13480-000");
        enderecoAntigo.setLogradouro("Rua A");
        enderecoAntigo.setNumero("100");
        enderecoAntigo.setComplemento("Casa");
        enderecoAntigo.setBairro("Centro");
        enderecoAntigo.setCidade("Rio Claro");
        enderecoAntigo.setEstado("SP");
        clienteExistente.setEndereco(enderecoAntigo);

        Cliente novosDados = new Cliente();
        novosDados.setNomeCompleto("João da Silva");
        novosDados.setNomeMae("Maria Aparecida");
        novosDados.setCpf("99999999999");
        novosDados.setRg("22222222");
        novosDados.setEmail("joao.silva@email.com");
        novosDados.setTelefone("19988888888");
        novosDados.setDataNascimento(LocalDate.of(1995, 5, 10));

        Endereco novoEndereco = new Endereco();
        novoEndereco.setCep("13500-000");
        novoEndereco.setLogradouro("Rua B");
        novoEndereco.setNumero("200");
        novoEndereco.setComplemento("Apto 10");
        novoEndereco.setBairro("Jardim");
        novoEndereco.setCidade("Limeira");
        novoEndereco.setEstado("SP");
        novosDados.setEndereco(novoEndereco);

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(clienteExistente));

        Mockito.when(repository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Cliente resultado = useCase.executar(id, novosDados);

        assertEquals(id, resultado.getId());
        assertEquals("João da Silva", resultado.getNomeCompleto());
        assertEquals("Maria Aparecida", resultado.getNomeMae());
        assertEquals("99999999999", resultado.getCpf());
        assertEquals("22222222", resultado.getRg());
        assertEquals("joao.silva@email.com", resultado.getEmail());
        assertEquals("19988888888", resultado.getTelefone());
        assertEquals(LocalDate.of(1995, 5, 10), resultado.getDataNascimento());

        assertNotNull(resultado.getEndereco());
        assertEquals("13500-000", resultado.getEndereco().getCep());
        assertEquals("Rua B", resultado.getEndereco().getLogradouro());
        assertEquals("200", resultado.getEndereco().getNumero());
        assertEquals("Apto 10", resultado.getEndereco().getComplemento());
        assertEquals("Jardim", resultado.getEndereco().getBairro());
        assertEquals("Limeira", resultado.getEndereco().getCidade());
        assertEquals("SP", resultado.getEndereco().getEstado());

        assertNotNull(resultado.getDataCadastro());
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoForEncontradoAoAtualizar() {
        ClienteRepository repository = Mockito.mock(ClienteRepository.class);
        AtualizarClienteUseCase useCase = new AtualizarClienteUseCase(repository);

        UUID id = UUID.randomUUID();
        Cliente novosDados = new Cliente();

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.empty());

        ClienteNaoEncontradoException exception = assertThrows(
                ClienteNaoEncontradoException.class,
                () -> useCase.executar(id, novosDados)
        );

        assertEquals("Cliente não encontrado.", exception.getMessage());
        Mockito.verify(repository).findById(id);
        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }
}
