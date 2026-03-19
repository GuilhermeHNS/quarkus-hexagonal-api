package com.guilhermehns.application.usecase;

import com.guilhermehns.domain.model.Cliente;
import com.guilhermehns.domain.repository.ClienteRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class BuscarClientePorIdUseCase {

    private final ClienteRepository repository;

    public BuscarClientePorIdUseCase(ClienteRepository repository) {
        this.repository = repository;
    }

    public Cliente executar(UUID id) {
        return repository.findById(id).orElseThrow();
    }
}
