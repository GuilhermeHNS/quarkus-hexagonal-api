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
        validarProduto(produto);
        produto.setId(UUID.randomUUID());
        produto.setDataCadastro(LocalDateTime.now());

        return repository.save(produto);
    }

    private void validarProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto é obrigatório");
        }

        validarCampoObrigatorio(produto.getNome(), "Nome do produto é obrigatório");
        validarCampoObrigatorio(produto.getTipo(), "Tipo do produto é obrigatório");
        validarCampoObrigatorio(produto.getDetalhes(), "Detalhes do produto são obrigatórios");

        if (produto.getDimensoes() == null) {
            throw new IllegalArgumentException("Dimensões do produto são obrigatórias");
        }

        if (produto.getDimensoes().getAltura() == null) {
            throw new IllegalArgumentException("Altura é obrigatória");
        }

        if (produto.getDimensoes().getLargura() == null) {
            throw new IllegalArgumentException("Largura é obrigatória");
        }

        if (produto.getDimensoes().getProfundidade() == null) {
            throw new IllegalArgumentException("Profundidade é obrigatória");
        }

        if (produto.getPeso() == null) {
            throw new IllegalArgumentException("Peso é obrigatório");
        }

        if (produto.getPrecoCompra() == null) {
            throw new IllegalArgumentException("Preço de compra é obrigatório");
        }

        if (produto.getPrecoVenda() == null) {
            throw new IllegalArgumentException("Preço de venda é obrigatório");
        }
    }

    private void validarCampoObrigatorio(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
    }
}
