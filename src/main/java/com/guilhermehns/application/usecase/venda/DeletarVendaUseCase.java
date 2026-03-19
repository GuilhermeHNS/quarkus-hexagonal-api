package com.guilhermehns.application.usecase.venda;

import com.guilhermehns.domain.repository.VendaRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class DeletarVendaUseCase {
    private final VendaRepository repository;

    public DeletarVendaUseCase(VendaRepository repository) {
        this.repository = repository;
    }

    public void executar(UUID id) {
        repository.findById(id).orElseThrow();
        repository.deleteById(id);
    }
}
