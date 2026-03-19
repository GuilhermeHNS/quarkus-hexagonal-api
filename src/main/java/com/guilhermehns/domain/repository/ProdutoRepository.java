package com.guilhermehns.domain.repository;

import com.guilhermehns.domain.model.produto.Produto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProdutoRepository {
    Produto save(Produto produto);

    List<Produto> findAllProdutos();

    Optional<Produto> findById(UUID id);

    void deleteById(UUID id);
}
