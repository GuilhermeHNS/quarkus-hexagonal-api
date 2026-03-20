package com.guilhermehns.application.usecase.cliente;

import com.guilhermehns.application.exception.ClienteNaoEncontradoException;
import com.guilhermehns.domain.model.cliente.Cliente;
import com.guilhermehns.domain.model.cliente.Endereco;
import com.guilhermehns.domain.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class AtualizarClienteUseCaseTest {

    private ClienteRepository repository;
    private AtualizarClienteUseCase useCase;


    @BeforeEach
    void mockaDependencias(){
        repository = Mockito.mock(ClienteRepository.class);
        useCase = new AtualizarClienteUseCase(repository);
    }

    @Test
    void deveAtualizarClienteComSucesso() {
        UUID clienteId = UUID.randomUUID();
        Cliente clienteExistente = criarClienteValido();
        Cliente clienteAtualizado = criarClienteValido();
        clienteAtualizado.setNomeCompleto("João Atualizado");

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));
        Mockito.when(repository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Cliente resultado = useCase.executar(clienteId, clienteAtualizado);

        assertEquals("João Atualizado", resultado.getNomeCompleto());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExistirParaAtualizacao() {
        UUID clienteId = UUID.randomUUID();
        Cliente cliente = criarClienteValido();

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.empty());

        ClienteNaoEncontradoException excecao = assertThrows(
                ClienteNaoEncontradoException.class,
                () -> useCase.executar(clienteId, cliente)
        );

        assertEquals("Cliente não encontrado.", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoClienteForNuloAoAtualizar() {
        UUID clienteId = UUID.randomUUID();
        Cliente clienteExistente = criarClienteValido();

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(clienteId, null)
        );

        assertEquals("Cliente é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoNomeCompletoForNuloAoAtualizar() {
        UUID clienteId = UUID.randomUUID();
        Cliente clienteExistente = criarClienteValido();
        Cliente cliente = criarClienteValido();
        cliente.setNomeCompleto(null);

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(clienteId, cliente)
        );

        assertEquals("Nome completo é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoNomeCompletoForVazioAoAtualizar() {
        UUID clienteId = UUID.randomUUID();
        Cliente clienteExistente = criarClienteValido();
        Cliente cliente = criarClienteValido();
        cliente.setNomeCompleto(" ");

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(clienteId, cliente)
        );

        assertEquals("Nome completo é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoNomeMaeForNuloAoAtualizar() {
        UUID clienteId = UUID.randomUUID();
        Cliente clienteExistente = criarClienteValido();
        Cliente cliente = criarClienteValido();
        cliente.setNomeMae(null);

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(clienteId, cliente)
        );

        assertEquals("Nome da mãe é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoCpfForNuloAoAtualizar() {
        UUID clienteId = UUID.randomUUID();
        Cliente clienteExistente = criarClienteValido();
        Cliente cliente = criarClienteValido();
        cliente.setCpf(null);

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(clienteId, cliente)
        );

        assertEquals("CPF é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoCpfForVazioAoAtualizar() {
        UUID clienteId = UUID.randomUUID();
        Cliente clienteExistente = criarClienteValido();
        Cliente cliente = criarClienteValido();
        cliente.setCpf(" ");

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(clienteId, cliente)
        );

        assertEquals("CPF é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoRgForNuloAoAtualizar() {
        UUID clienteId = UUID.randomUUID();
        Cliente clienteExistente = criarClienteValido();
        Cliente cliente = criarClienteValido();
        cliente.setRg(null);

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(clienteId, cliente)
        );

        assertEquals("RG é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoEmailForNuloAoAtualizar() {
        UUID clienteId = UUID.randomUUID();
        Cliente clienteExistente = criarClienteValido();
        Cliente cliente = criarClienteValido();
        cliente.setEmail(null);

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(clienteId, cliente)
        );

        assertEquals("Email é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoEmailForVazioAoAtualizar() {
        UUID clienteId = UUID.randomUUID();
        Cliente clienteExistente = criarClienteValido();
        Cliente cliente = criarClienteValido();
        cliente.setEmail(" ");

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(clienteId, cliente)
        );

        assertEquals("Email é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoTelefoneForNuloAoAtualizar() {
        UUID clienteId = UUID.randomUUID();
        Cliente clienteExistente = criarClienteValido();
        Cliente cliente = criarClienteValido();
        cliente.setTelefone(null);

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(clienteId, cliente)
        );

        assertEquals("Telefone é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoTelefoneForVazioAoAtualizar() {
        UUID clienteId = UUID.randomUUID();
        Cliente clienteExistente = criarClienteValido();
        Cliente cliente = criarClienteValido();
        cliente.setTelefone(" ");

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(clienteId, cliente)
        );

        assertEquals("Telefone é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoDataNascimentoForNulaAoAtualizar() {
        UUID clienteId = UUID.randomUUID();
        Cliente clienteExistente = criarClienteValido();
        Cliente cliente = criarClienteValido();
        cliente.setDataNascimento(null);

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(clienteId, cliente)
        );

        assertEquals("Data de nascimento é obrigatória", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoEnderecoForNuloAoAtualizar() {
        UUID clienteId = UUID.randomUUID();
        Cliente clienteExistente = criarClienteValido();
        Cliente cliente = criarClienteValido();
        cliente.setEndereco(null);

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(clienteId, cliente)
        );

        assertEquals("Endereço é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoCepForNuloAoAtualizar() {
        UUID clienteId = UUID.randomUUID();
        Cliente clienteExistente = criarClienteValido();
        Cliente cliente = criarClienteValido();
        cliente.getEndereco().setCep(null);

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(clienteId, cliente)
        );

        assertEquals("CEP é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoCepForVazioAoAtualizar() {
        UUID clienteId = UUID.randomUUID();
        Cliente clienteExistente = criarClienteValido();
        Cliente cliente = criarClienteValido();
        cliente.getEndereco().setCep(" ");

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(clienteId, cliente)
        );

        assertEquals("CEP é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoLogradouroForNuloAoAtualizar() {
        UUID clienteId = UUID.randomUUID();
        Cliente clienteExistente = criarClienteValido();
        Cliente cliente = criarClienteValido();
        cliente.getEndereco().setLogradouro(null);

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(clienteId, cliente)
        );

        assertEquals("Logradouro é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoNumeroForNuloAoAtualizar() {
        UUID clienteId = UUID.randomUUID();
        Cliente clienteExistente = criarClienteValido();
        Cliente cliente = criarClienteValido();
        cliente.getEndereco().setNumero(null);

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(clienteId, cliente)
        );

        assertEquals("Número é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoBairroForNuloAoAtualizar() {
        UUID clienteId = UUID.randomUUID();
        Cliente clienteExistente = criarClienteValido();
        Cliente cliente = criarClienteValido();
        cliente.getEndereco().setBairro(null);

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(clienteId, cliente)
        );

        assertEquals("Bairro é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoCidadeForNulaAoAtualizar() {
        UUID clienteId = UUID.randomUUID();
        Cliente clienteExistente = criarClienteValido();
        Cliente cliente = criarClienteValido();
        cliente.getEndereco().setCidade(null);

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(clienteId, cliente)
        );

        assertEquals("Cidade é obrigatória", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoEstadoForNuloAoAtualizar() {
        UUID clienteId = UUID.randomUUID();
        Cliente clienteExistente = criarClienteValido();
        Cliente cliente = criarClienteValido();
        cliente.getEndereco().setEstado(null);

        Mockito.when(repository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(clienteId, cliente)
        );

        assertEquals("Estado é obrigatório", excecao.getMessage());
        Mockito.verify(repository, Mockito.times(1)).findById(clienteId);
        verifyNoMoreInteractions(repository);
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
