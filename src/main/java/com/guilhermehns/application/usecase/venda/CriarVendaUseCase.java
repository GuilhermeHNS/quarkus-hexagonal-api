package com.guilhermehns.application.usecase.venda;

import com.guilhermehns.application.exception.ClienteNaoEncontradoException;
import com.guilhermehns.application.exception.ProdutoNaoEncontradoException;
import com.guilhermehns.domain.model.venda.ItemVenda;
import com.guilhermehns.domain.model.venda.Venda;
import com.guilhermehns.domain.repository.ClienteRepository;
import com.guilhermehns.domain.repository.ProdutoRepository;
import com.guilhermehns.domain.repository.VendaRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class CriarVendaUseCase {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public CriarVendaUseCase(VendaRepository vendaRepository,
                             ClienteRepository clienteRepository,
                             ProdutoRepository produtoRepository) {
        this.vendaRepository = vendaRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    public Venda executar(Venda venda) {
        validarVenda(venda);
        validarExistenciaClienteEProdutos(venda);
        validarFormaPagamento(venda);

        venda.setId(UUID.randomUUID());
        return vendaRepository.save(venda);
    }

    private void validarExistenciaClienteEProdutos(Venda venda) {
        UUID clienteId = venda.getCliente().getId();
        clienteRepository.findById(clienteId).orElseThrow(ClienteNaoEncontradoException::new);

        for (ItemVenda item : venda.getItens()) {
            UUID produtoId = item.getProduto().getId();
            produtoRepository.findById(produtoId).orElseThrow(ProdutoNaoEncontradoException::new);
        }
    }

    private void validarVenda(Venda venda) {
        if (venda == null) {
            throw new IllegalArgumentException("Venda é obrigatória.");
        }
        if (venda.getCliente() == null) {
            throw new IllegalArgumentException("Cliente é obrigatório.");
        }
        if (venda.getCodigoVendedor() == null || venda.getCodigoVendedor().isBlank()) {
            throw new IllegalArgumentException("Código do vendedor é obrigatório.");
        }
        if (venda.getItens() == null || venda.getItens().isEmpty()) {
            throw new IllegalArgumentException("Itens da venda são obrigatórios.");
        }

        for (ItemVenda item : venda.getItens()) {
            if (item.getProduto() == null) {
                throw new IllegalArgumentException("Produto do item é obrigatório.");
            }
            if (item.getQuantidade() == null) {
                throw new IllegalArgumentException("Quantidade do item é obrigatória.");
            }
            if (item.getValorUnitario() == null) {
                throw new IllegalArgumentException("Valor unitário do item é obrigatório.");
            }
        }
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
