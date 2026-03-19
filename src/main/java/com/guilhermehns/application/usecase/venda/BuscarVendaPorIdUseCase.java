package com.guilhermehns.application.usecase.venda;

import com.guilhermehns.domain.model.venda.Venda;
import com.guilhermehns.domain.repository.VendaRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class BuscarVendaPorIdUseCase {
    private final VendaRepository repository;

    public BuscarVendaPorIdUseCase(VendaRepository repository) {
        this.repository = repository;
    }

    public Venda executar(UUID id) {
        return repository.findById(id).orElseThrow();
    }
}
