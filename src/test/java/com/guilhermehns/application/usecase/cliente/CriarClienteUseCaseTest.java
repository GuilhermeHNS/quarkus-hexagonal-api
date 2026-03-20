package com.guilhermehns.application.usecase.cliente;

import com.guilhermehns.domain.model.cliente.Cliente;
import com.guilhermehns.domain.model.cliente.Endereco;
import com.guilhermehns.domain.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;

public class CriarClienteUseCaseTest {

    private ClienteRepository repository;
    private CriarClienteUseCase useCase;

    @BeforeEach
    void mockaDependencias() {
        repository = Mockito.mock(ClienteRepository.class);
        useCase = new CriarClienteUseCase(repository);
    }

    @Test
    void deveCriarClienteComSucesso() {
        Cliente cliente = criarClienteValido();

        Mockito.when(repository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Cliente resultado = useCase.executar(cliente);

        assertNotNull(resultado.getId());
        assertNotNull(resultado.getDataCadastro());
        assertEquals("João Silva", resultado.getNomeCompleto());
        Mockito.verify(repository, Mockito.times(1)).save(cliente);
        Mockito.verifyNoMoreInteractions(repository);

    }

    @Test
    void deveLancarExcecaoQuandoClienteForNulo() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(null)
        );

        assertEquals("Cliente é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoNomeCompletoForNulo() {
        Cliente cliente = criarClienteValido();
        cliente.setNomeCompleto(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(cliente)
        );

        assertEquals("Nome completo é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoNomeCompletoForVazio() {

        Cliente cliente = criarClienteValido();
        cliente.setNomeCompleto(" ");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(cliente)
        );

        assertEquals("Nome completo é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoNomeMaeForNulo() {

        Cliente cliente = criarClienteValido();
        cliente.setNomeMae(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(cliente)
        );

        assertEquals("Nome da mãe é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoCpfForNulo() {

        Cliente cliente = criarClienteValido();
        cliente.setCpf(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(cliente)
        );

        assertEquals("CPF é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoRgForNulo() {

        Cliente cliente = criarClienteValido();
        cliente.setRg(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(cliente)
        );

        assertEquals("RG é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoEmailForNulo() {

        Cliente cliente = criarClienteValido();
        cliente.setEmail(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(cliente)
        );

        assertEquals("Email é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoTelefoneForNulo() {

        Cliente cliente = criarClienteValido();
        cliente.setTelefone(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(cliente)
        );

        assertEquals("Telefone é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoDataNascimentoForNula() {

        Cliente cliente = criarClienteValido();
        cliente.setDataNascimento(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(cliente)
        );

        assertEquals("Data de nascimento é obrigatória", excecao.getMessage());
        verifyNoInteractions(repository);
    }


    @Test
    void deveLancarExcecaoQuandoEnderecoForNulo() {

        Cliente cliente = criarClienteValido();
        cliente.setEndereco(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(cliente)
        );

        assertEquals("Endereço é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoCepForNulo() {

        Cliente cliente = criarClienteValido();
        cliente.getEndereco().setCep(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(cliente)
        );

        assertEquals("CEP é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoLogradouroForNulo() {

        Cliente cliente = criarClienteValido();
        cliente.getEndereco().setLogradouro(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(cliente)
        );

        assertEquals("Logradouro é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoNumeroForNulo() {

        Cliente cliente = criarClienteValido();
        cliente.getEndereco().setNumero(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(cliente)
        );

        assertEquals("Número é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoBairroForNulo() {

        Cliente cliente = criarClienteValido();
        cliente.getEndereco().setBairro(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(cliente)
        );

        assertEquals("Bairro é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoCidadeForNula() {

        Cliente cliente = criarClienteValido();
        cliente.getEndereco().setCidade(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(cliente)
        );

        assertEquals("Cidade é obrigatória", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoEstadoForNulo() {

        Cliente cliente = criarClienteValido();
        cliente.getEndereco().setEstado(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(cliente)
        );

        assertEquals("Estado é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoCpfForVazio() {
        Cliente cliente = criarClienteValido();
        cliente.setCpf(" ");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(cliente)
        );

        assertEquals("CPF é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoEmailForVazio() {
        Cliente cliente = criarClienteValido();
        cliente.setEmail(" ");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(cliente)
        );

        assertEquals("Email é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoTelefoneForVazio() {
        Cliente cliente = criarClienteValido();
        cliente.setTelefone(" ");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(cliente)
        );

        assertEquals("Telefone é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoCepForVazio() {
        Cliente cliente = criarClienteValido();
        cliente.getEndereco().setCep(" ");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(cliente)
        );

        assertEquals("CEP é obrigatório", excecao.getMessage());
        verifyNoInteractions(repository);
    }

    private Cliente criarClienteValido() {
        Cliente cliente = new Cliente();
        cliente.setNomeCompleto("João Silva");
        cliente.setNomeMae("Maria Souza");
        cliente.setCpf("12345678900");
        cliente.setRg("123456789");
        cliente.setEmail("guilherme@email.com");
        cliente.setTelefone("19999999999");
        cliente.setDataNascimento(LocalDate.of(2000, 1, 1));

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
