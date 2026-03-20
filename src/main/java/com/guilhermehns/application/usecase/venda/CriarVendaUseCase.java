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

import static com.guilhermehns.application.util.VendaValidationHelper.*;

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
        validarExistenciaClienteEProdutos(venda, clienteRepository, produtoRepository);
        validarFormaPagamento(venda);

        venda.setId(UUID.randomUUID());
        return vendaRepository.save(venda);
    }

}
