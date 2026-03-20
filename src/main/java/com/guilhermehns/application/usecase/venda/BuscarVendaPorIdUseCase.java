package com.guilhermehns.application.usecase.venda;

import com.guilhermehns.application.exception.VendaNaoEncontradaException;
import com.guilhermehns.application.util.VendaCalculationHelper;
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
        Venda venda = repository.findById(id)
                .orElseThrow(VendaNaoEncontradaException::new);

        return VendaCalculationHelper.calcularTotais((venda));
    }

}
