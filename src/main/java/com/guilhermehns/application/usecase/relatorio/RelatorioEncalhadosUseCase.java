package com.guilhermehns.application.usecase.relatorio;

import com.guilhermehns.application.dto.EncalhadoDTO;
import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.repository.ProdutoRepository;
import com.guilhermehns.domain.repository.VendaRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class RelatorioEncalhadosUseCase {

    private final ProdutoRepository produtoRepository;
    private final VendaRepository vendaRepository;

    public RelatorioEncalhadosUseCase(ProdutoRepository produtoRepository, VendaRepository vendaRepository) {
        this.produtoRepository = produtoRepository;
        this.vendaRepository = vendaRepository;
    }

    public List<EncalhadoDTO> executar() {
        LocalDateTime limite = LocalDateTime.now().minusDays(30);

        Set<UUID> produtosVendidosRecentemente = vendaRepository.findAllVendas().stream()
                .filter(venda -> venda.getDataVenda() != null && venda.getDataVenda().isAfter(limite))
                .flatMap(venda -> venda.getItens().stream())
                .map(item -> item.getProduto().getId())
                .collect(Collectors.toSet());

        return produtoRepository.findAllProdutos().stream()
                .filter(produto -> !produtosVendidosRecentemente.contains(produto.getId()))
                .map(this::toDto)
                .toList();
    }

    private EncalhadoDTO toDto(Produto produto) {
        return new EncalhadoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDataCadastro()
        );
    }
}