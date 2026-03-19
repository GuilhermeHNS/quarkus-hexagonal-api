package com.guilhermehns.application.usecase.venda;
import com.guilhermehns.domain.model.venda.Venda;
import com.guilhermehns.domain.repository.VendaRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class AtualizarVendaUseCase {

    private final VendaRepository repository;

    public AtualizarVendaUseCase(VendaRepository repository) {
        this.repository = repository;
    }

    public Venda executar(UUID id, Venda vendaAtualizada) {
        Venda vendaExistente = repository.findById(id).orElseThrow();

        vendaExistente.setCliente(vendaAtualizada.getCliente());
        vendaExistente.setCodigoVendedor(vendaAtualizada.getCodigoVendedor());
        vendaExistente.setDataVenda(vendaAtualizada.getDataVenda());
        vendaExistente.setFormaPagamento(vendaAtualizada.getFormaPagamento());
        vendaExistente.setNumeroCartao(vendaAtualizada.getNumeroCartao());
        vendaExistente.setValorPago(vendaAtualizada.getValorPago());
        vendaExistente.setItens(vendaAtualizada.getItens());

        return repository.save(vendaExistente);
    }
}