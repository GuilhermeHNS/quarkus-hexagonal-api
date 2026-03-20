package com.guilhermehns.domain.repository;

import com.guilhermehns.application.dto.NovoClienteDTO;
import com.guilhermehns.domain.model.cliente.Cliente;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository {
    Cliente save(Cliente cliente);

    Optional<Cliente> findById(UUID id);

    List<Cliente> findAllClientes();

    void deleteById(UUID id);

    List<NovoClienteDTO> buscarNovosClientesPorAno(int ano);
}
