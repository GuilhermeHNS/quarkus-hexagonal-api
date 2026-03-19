package com.guilhermehns.application.usecase.produto;

import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.repository.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class AtualizarProdutoUseCase {

    private final ProdutoRepository repository;

    public AtualizarProdutoUseCase(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Produto executar(UUID id, Produto produtoAtualizado) {
        Produto produtoExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produtoExistente.setNome(produtoAtualizado.getNome());
        produtoExistente.setTipo(produtoAtualizado.getTipo());
        produtoExistente.setDetalhes(produtoAtualizado.getDetalhes());
        produtoExistente.setPeso(produtoAtualizado.getPeso());
        produtoExistente.setPrecoCompra(produtoAtualizado.getPrecoCompra());
        produtoExistente.setPrecoVenda(produtoAtualizado.getPrecoVenda());
        produtoExistente.setDimensoes(produtoAtualizado.getDimensoes());

        return repository.save(produtoExistente);
    }
}
