package com.guilhermehns.application.usecase.venda;

import com.guilhermehns.domain.model.venda.Venda;
import com.guilhermehns.domain.repository.VendaRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class CriarVendaUseCase {

    private final VendaRepository repository;

    public CriarVendaUseCase(VendaRepository repository) {
        this.repository = repository;
    }

    public Venda executar(Venda venda) {
        venda.setId(UUID.randomUUID());
        return repository.save(venda);
    }
}
