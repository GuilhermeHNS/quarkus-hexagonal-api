package com.guilhermehns.domain.repository;

import com.guilhermehns.domain.model.venda.Venda;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VendaRepository {
    Venda save(Venda venda);

    List<Venda> findAllVendas();

    Optional<Venda> findById(UUID id);

    void deleteById(UUID id);
}
