package com.guilhermehns.domain.repository;

import com.guilhermehns.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository {
    Cliente save(Cliente cliente);

    Optional<Cliente> getById(String id);

    List<Cliente> list();

    void deleteById(String id);
}
