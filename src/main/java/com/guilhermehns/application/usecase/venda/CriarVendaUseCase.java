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
        validarFormaPagamento(venda);
        venda.setId(UUID.randomUUID());
        return repository.save(venda);
    }

    private void validarFormaPagamento(Venda venda) {
        if (venda.getFormaPagamento() == null || venda.getFormaPagamento().isBlank()) {
            throw new IllegalArgumentException("Forma de pagamento é obrigatória.");
        }

        String formaPagamento = venda.getFormaPagamento().trim().toUpperCase();

        if (!"DINHEIRO".equals(formaPagamento) && !"CARTAO_CREDITO".equals(formaPagamento)) {
            throw new IllegalArgumentException("Forma de pagamento inválida. Use DINHEIRO ou CARTAO_CREDITO.");
        }

        if ("CARTAO_CREDITO".equals(formaPagamento)
                && (venda.getNumeroCartao() == null || venda.getNumeroCartao().isBlank())) {
            throw new IllegalArgumentException("Número do cartão é obrigatório para pagamentos com cartão.");
        }

        if (("DINHEIRO".equals(formaPagamento))
                && venda.getValorPago() == null) {
            throw new IllegalArgumentException("Valor pago é obrigatório para pagamentos em dinheiro.");
        }
    }
}
