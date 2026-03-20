package com.guilhermehns.application.usecase.relatorio;

import com.guilhermehns.application.dto.EncalhadoDTO;
import com.guilhermehns.domain.repository.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class RelatorioEncalhadosUseCase {

    private final ProdutoRepository produtoRepository;

    public RelatorioEncalhadosUseCase(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<EncalhadoDTO> executar() {
        return produtoRepository.buscarProdutosEncalhados();
    }

}