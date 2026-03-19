package com.guilhermehns.application.usecase.produto;

import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.repository.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ListarProdutosUseCase {

    private final ProdutoRepository repository;

    public ListarProdutosUseCase(ProdutoRepository repository) {
        this.repository = repository;
    }

    public List<Produto> executar() {
        return repository.findAllProdutos();
    }
}