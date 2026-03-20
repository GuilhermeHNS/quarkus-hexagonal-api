package com.guilhermehns.application.usecase.cliente;

import com.guilhermehns.application.exception.ClienteNaoEncontradoException;
import com.guilhermehns.domain.model.cliente.Cliente;
import com.guilhermehns.domain.model.cliente.Endereco;
import com.guilhermehns.domain.repository.ClienteRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

import static com.guilhermehns.application.util.ClienteValidationHelper.validarCliente;

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
}
