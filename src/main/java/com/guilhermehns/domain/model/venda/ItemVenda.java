package com.guilhermehns.domain.model.venda;

import com.guilhermehns.domain.model.produto.Produto;

import java.math.BigDecimal;

public class ItemVenda {
    private Produto produto;
    private Integer quantidade;
    private BigDecimal valorUnitario;

    public ItemVenda() {
    }

    public ItemVenda(Produto produto, Integer quantidade, BigDecimal valorUnitario) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }
}
