package com.guilhermehns.application.usecase.cliente;

import com.guilhermehns.application.exception.ClienteNaoEncontradoException;
import com.guilhermehns.domain.model.cliente.Cliente;
import com.guilhermehns.domain.model.cliente.Endereco;
import com.guilhermehns.domain.repository.ClienteRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class AtualizarClienteUseCase {

    private final ClienteRepository repository;

    public AtualizarClienteUseCase(ClienteRepository repository) {
        this.repository = repository;
    }

    public Cliente executar(UUID id, Cliente novosDados) {
        Cliente clienteExistente = repository.findById(id).orElseThrow(ClienteNaoEncontradoException::new);
        validarCliente(novosDados);

        clienteExistente.setNomeCompleto(novosDados.getNomeCompleto());
        clienteExistente.setNomeMae(novosDados.getNomeMae());
        clienteExistente.setCpf(novosDados.getCpf());
        clienteExistente.setRg(novosDados.getRg());
        clienteExistente.setEmail(novosDados.getEmail());
        clienteExistente.setTelefone(novosDados.getTelefone());
        clienteExistente.setDataNascimento(novosDados.getDataNascimento());
        clienteExistente.setEndereco(novosDados.getEndereco());

        return repository.save(clienteExistente);

    }

    private void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }

        validarCampoObrigatorio(cliente.getNomeCompleto(), "Nome completo é obrigatório");
        validarCampoObrigatorio(cliente.getNomeMae(), "Nome da mãe é obrigatório");
        validarCampoObrigatorio(cliente.getCpf(), "CPF é obrigatório");
        validarCampoObrigatorio(cliente.getRg(), "RG é obrigatório");
        validarCampoObrigatorio(cliente.getEmail(), "Email é obrigatório");
        validarCampoObrigatorio(cliente.getTelefone(), "Telefone é obrigatório");

        if (cliente.getDataNascimento() == null) {
            throw new IllegalArgumentException("Data de nascimento é obrigatória");
        }

        validarEndereco(cliente.getEndereco());
    }

    private void validarCampoObrigatorio(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
    }

    private void validarEndereco(Endereco endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço é obrigatório");
        }

        validarCampoObrigatorio(endereco.getCep(), "CEP é obrigatório");
        validarCampoObrigatorio(endereco.getLogradouro(), "Logradouro é obrigatório");
        validarCampoObrigatorio(endereco.getNumero(), "Número é obrigatório");
        validarCampoObrigatorio(endereco.getBairro(), "Bairro é obrigatório");
        validarCampoObrigatorio(endereco.getCidade(), "Cidade é obrigatória");
        validarCampoObrigatorio(endereco.getEstado(), "Estado é obrigatório");
    }
}
