package com.guilhermehns.adapters.out.persistence.mongo.cliente;

import com.guilhermehns.application.dto.NovoClienteDTO;
import com.guilhermehns.domain.model.cliente.Cliente;
import com.guilhermehns.domain.model.cliente.Endereco;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ClienteRepositoryImplTest {

    @Inject
    ClienteRepositoryImpl clienteRepository;

    @BeforeEach
    void setUp() {
        clienteRepository.deleteAll();
    }

    @Test
    void deveSalvarClienteComSucesso() {
        Cliente cliente = criarClienteValido();

        Cliente resultado = clienteRepository.save(cliente);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals(cliente.getNomeCompleto(), resultado.getNomeCompleto());
        assertEquals(cliente.getCpf(), resultado.getCpf());
        assertEquals(cliente.getEmail(), resultado.getEmail());
        assertNotNull(resultado.getEndereco());
        assertEquals(cliente.getEndereco().getCep(), resultado.getEndereco().getCep());
    }

    @Test
    void deveBuscarClientePorIdComSucesso() {
        Cliente cliente = criarClienteValido();
        clienteRepository.save(cliente);

        Optional<Cliente> resultado = clienteRepository.findById(cliente.getId());

        assertTrue(resultado.isPresent());
        assertEquals(cliente.getId(), resultado.get().getId());
        assertEquals(cliente.getNomeCompleto(), resultado.get().getNomeCompleto());
        assertEquals(cliente.getCpf(), resultado.get().getCpf());
    }

    @Test
    void deveRetornarVazioQuandoClienteNaoExistir() {
        Optional<Cliente> resultado = clienteRepository.findById(UUID.randomUUID());

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveListarTodosOsClientes() {
        Cliente cliente1 = criarClienteValido();
        Cliente cliente2 = criarClienteValido();
        cliente2.setId(UUID.randomUUID());
        cliente2.setNomeCompleto("Maria Oliveira");
        cliente2.setCpf("99999999999");
        cliente2.setEmail("maria@email.com");

        clienteRepository.save(cliente1);
        clienteRepository.save(cliente2);

        List<Cliente> resultado = clienteRepository.findAllClientes();

        assertEquals(2, resultado.size());
    }

    @Test
    void deveDeletarClienteComSucesso() {
        Cliente cliente = criarClienteValido();
        clienteRepository.save(cliente);

        clienteRepository.deleteById(cliente.getId());

        Optional<Cliente> resultado = clienteRepository.findById(cliente.getId());

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveBuscarNovosClientesPorAnoComSucesso() {
        Cliente cliente2025 = criarClienteValido();
        cliente2025.setDataCadastro(LocalDateTime.of(2025, 3, 20, 10, 0));

        Cliente cliente2024 = criarClienteValido();
        cliente2024.setId(UUID.randomUUID());
        cliente2024.setCpf("88888888888");
        cliente2024.setEmail("outro@email.com");
        cliente2024.setDataCadastro(LocalDateTime.of(2024, 3, 20, 10, 0));

        clienteRepository.save(cliente2025);
        clienteRepository.save(cliente2024);

        List<NovoClienteDTO> resultado = clienteRepository.buscarNovosClientesPorAno(2025);

        assertEquals(1, resultado.size());
        assertEquals(cliente2025.getId(), resultado.get(0).getClienteId());
        assertEquals(cliente2025.getNomeCompleto(), resultado.get(0).getNomeCompleto());
        assertEquals(cliente2025.getDataNascimento(), resultado.get(0).getDataNascimento());
        assertEquals(cliente2025.getDataCadastro(), resultado.get(0).getDataCadastro());
    }

    private Cliente criarClienteValido() {
        Cliente cliente = new Cliente();
        cliente.setId(UUID.randomUUID());
        cliente.setNomeCompleto("João Silva");
        cliente.setNomeMae("Maria Souza");
        cliente.setCpf("12345678900");
        cliente.setRg("123456789");
        cliente.setEmail("joao@email.com");
        cliente.setTelefone("19999999999");
        cliente.setDataNascimento(LocalDate.of(2000, 1, 1));
        cliente.setDataCadastro(LocalDateTime.of(2026, 3, 20, 10, 0));

        Endereco endereco = new Endereco();
        endereco.setCep("13400-000");
        endereco.setLogradouro("Rua A");
        endereco.setNumero("123");
        endereco.setComplemento("Casa");
        endereco.setBairro("Centro");
        endereco.setCidade("Limeira");
        endereco.setEstado("SP");

        cliente.setEndereco(endereco);
        return cliente;
    }
}