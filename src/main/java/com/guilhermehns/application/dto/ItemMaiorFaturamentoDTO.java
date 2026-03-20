package com.guilhermehns.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ItemMaiorFaturamentoDTO {
    private UUID produtoId;
    private String nomeProduto;
    private BigDecimal precoVenda;

    public ItemMaiorFaturamentoDTO() {
    }

    public ItemMaiorFaturamentoDTO(UUID produtoId, String nomeProduto, BigDecimal precoVenda) {
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.precoVenda = precoVenda;
    }

    public UUID getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(UUID produtoId) {
        this.produtoId = produtoId;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }
}