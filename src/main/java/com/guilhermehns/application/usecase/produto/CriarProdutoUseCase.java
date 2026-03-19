package com.guilhermehns.application.usecase.produto;

import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.repository.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
public class CriarProdutoUseCase {
    private final ProdutoRepository repository;

    public CriarProdutoUseCase(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Produto executar(Produto produto) {

        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new RuntimeException("Nome é obrigatório");
        }

        if (produto.getTipo() == null || produto.getTipo().isBlank()) {
            throw new RuntimeException("Tipo é obrigatório");
        }

        if(produto.getPrecoCompra() == null ){
            throw new RuntimeException("Preço de compra é obrigatório");
        }

        if(produto.getPrecoVenda() == null ){
            throw new RuntimeException("Preço de compra é obrigatório");
        }

        produto.setId(UUID.randomUUID());
        produto.setDataCadastro(LocalDateTime.now());

        return repository.save(produto);
    }
}
