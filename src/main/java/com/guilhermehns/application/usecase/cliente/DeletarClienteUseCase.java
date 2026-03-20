package com.guilhermehns.application.usecase.cliente;

import com.guilhermehns.application.exception.ClienteNaoEncontradoException;
import com.guilhermehns.domain.repository.ClienteRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class DeletarClienteUseCase {

    private final ClienteRepository repository;

    public DeletarClienteUseCase(ClienteRepository repository) {
        this.repository = repository;
    }

    public void executar(UUID id) {
        repository.findById(id).orElseThrow(ClienteNaoEncontradoException::new);
        repository.deleteById(id);
    }
}
