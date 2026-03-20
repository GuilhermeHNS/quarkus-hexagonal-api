package com.guilhermehns.application.usecase.venda;

import com.guilhermehns.application.util.VendaCalculationHelper;
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
        List<Venda> vendas = repository.findAllVendas();
        return vendas.stream()
                .map(VendaCalculationHelper::calcularTotais)
                .toList();
    }
}
