package com.guilhermehns.application.usecase.produto;

import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.repository.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.guilhermehns.application.util.ProdutoValidationHelper.validarProduto;

@ApplicationScoped
public class CriarProdutoUseCase {
    private final ProdutoRepository repository;

    public CriarProdutoUseCase(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Produto executar(Produto produto) {
        validarProduto(produto);
        produto.setId(UUID.randomUUID());
        produto.setDataCadastro(LocalDateTime.now());

        return repository.save(produto);
    }


}
