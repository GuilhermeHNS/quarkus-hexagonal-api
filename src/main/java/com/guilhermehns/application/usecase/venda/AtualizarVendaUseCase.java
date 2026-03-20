package com.guilhermehns.application.usecase.venda;

import com.guilhermehns.application.exception.ClienteNaoEncontradoException;
import com.guilhermehns.application.exception.ProdutoNaoEncontradoException;
import com.guilhermehns.application.exception.VendaNaoEncontradaException;
import com.guilhermehns.application.util.VendaValidationHelper;
import com.guilhermehns.domain.model.venda.ItemVenda;
import com.guilhermehns.domain.model.venda.Venda;
import com.guilhermehns.domain.repository.ClienteRepository;
import com.guilhermehns.domain.repository.ProdutoRepository;
import com.guilhermehns.domain.repository.VendaRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

import static com.guilhermehns.application.util.VendaValidationHelper.*;

@ApplicationScoped
public class AtualizarVendaUseCase {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public AtualizarVendaUseCase(VendaRepository vendaRepository,
                                 ClienteRepository clienteRepository,
                                 ProdutoRepository produtoRepository) {
        this.vendaRepository = vendaRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    public Venda executar(UUID vendaId, Venda vendaAtualizada) {
        validarVenda(vendaAtualizada);
        validarExistenciaClienteEProdutos(vendaAtualizada, clienteRepository, produtoRepository);
        validarFormaPagamento(vendaAtualizada);

        Venda vendaExistente = vendaRepository.findById(vendaId)
                .orElseThrow(VendaNaoEncontradaException::new);

        vendaExistente.setCliente(vendaAtualizada.getCliente());
        vendaExistente.setCodigoVendedor(vendaAtualizada.getCodigoVendedor());
        vendaExistente.setDataVenda(vendaAtualizada.getDataVenda());
        vendaExistente.setFormaPagamento(vendaAtualizada.getFormaPagamento());
        vendaExistente.setNumeroCartao(vendaAtualizada.getNumeroCartao());
        vendaExistente.setValorPago(vendaAtualizada.getValorPago());
        vendaExistente.setItens(vendaAtualizada.getItens());

        return vendaRepository.save(vendaExistente);
    }

}