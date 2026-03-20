package com.guilhermehns.application.usecase.cliente;

import com.guilhermehns.domain.model.cliente.Cliente;
import com.guilhermehns.domain.model.cliente.Endereco;
import com.guilhermehns.domain.repository.ClienteRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.guilhermehns.application.util.ClienteValidationHelper.validarCliente;

@ApplicationScoped
public class CriarClienteUseCase {

    private final ClienteRepository repository;

    public CriarClienteUseCase(ClienteRepository repository) {
        this.repository = repository;
    }

    public Cliente executar(Cliente cliente) {
        validarCliente(cliente);
        cliente.setId(UUID.randomUUID());
        cliente.setDataCadastro(LocalDateTime.now());
        return repository.save(cliente);
    }

}
