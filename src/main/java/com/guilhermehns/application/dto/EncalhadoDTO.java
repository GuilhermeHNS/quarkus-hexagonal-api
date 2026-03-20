package com.guilhermehns.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class EncalhadoDTO {
    private UUID produtoId;
    private String nomeProduto;
    private BigDecimal peso;
    private BigDecimal precoCompra;
    private LocalDateTime dataCadastro;

    public EncalhadoDTO() {
    }

    public EncalhadoDTO(UUID produtoId, String nomeProduto, BigDecimal peso, BigDecimal precoCompra, LocalDateTime dataCadastro) {
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.peso = peso;
        this.precoCompra = precoCompra;
        this.dataCadastro = dataCadastro;
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

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(BigDecimal precoCompra) {
        this.precoCompra = precoCompra;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}