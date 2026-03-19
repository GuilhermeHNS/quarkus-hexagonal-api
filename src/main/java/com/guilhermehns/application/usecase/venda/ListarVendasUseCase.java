package com.guilhermehns.application.usecase.venda;

import com.guilhermehns.domain.model.venda.Venda;
import com.guilhermehns.domain.repository.VendaRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ListarVendasUseCase {
    private final VendaRepository repository;

    public ListarVendasUseCase(VendaRepository repository) {
        this.repository = repository;
    }

    public List<Venda> executar() {
        return repository.findAllVendas();
    }
}
