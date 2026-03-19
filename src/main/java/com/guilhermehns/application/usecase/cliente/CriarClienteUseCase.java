package com.guilhermehns.application.usecase.cliente;

import com.guilhermehns.domain.model.cliente.Cliente;
import com.guilhermehns.domain.repository.ClienteRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
public class CriarClienteUseCase {

    private final ClienteRepository repository;

    public CriarClienteUseCase(ClienteRepository repository) {
        this.repository = repository;
    }

    public Cliente executar(Cliente cliente) {
        if (cliente.getNomeCompleto() == null || cliente.getNomeCompleto().isBlank()) {
            throw new RuntimeException("Nome é obrigatório");
        }

        if (cliente.getCpf() == null || cliente.getCpf().isBlank()) {
            throw new RuntimeException("CPF é obrigatório");
        }

        cliente.setId(UUID.randomUUID());
        cliente.setDataCadastro(LocalDateTime.now());

        return repository.save(cliente);
    }
}
