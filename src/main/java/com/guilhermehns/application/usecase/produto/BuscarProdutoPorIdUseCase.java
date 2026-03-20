package com.guilhermehns.application.usecase.produto;

import com.guilhermehns.application.exception.ProdutoNaoEncontradoException;
import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.repository.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class BuscarProdutoPorIdUseCase {

    private final ProdutoRepository repository;

    public BuscarProdutoPorIdUseCase(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Produto executar(UUID id) {
        return repository.findById(id).orElseThrow(ProdutoNaoEncontradoException::new);
    }
}
