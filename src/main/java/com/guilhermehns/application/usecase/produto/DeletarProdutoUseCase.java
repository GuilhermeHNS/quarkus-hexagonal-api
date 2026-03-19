package com.guilhermehns.application.usecase.produto;

import com.guilhermehns.domain.repository.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class DeletarProdutoUseCase {
    private final ProdutoRepository repository;

    public DeletarProdutoUseCase(ProdutoRepository repository) {
        this.repository = repository;
    }

    public void executar(UUID id) {
        repository.findById(id).orElseThrow();
        repository.deleteById(id);
    }
}
