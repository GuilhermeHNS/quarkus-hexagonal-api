package com.guilhermehns.domain.model.produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Produto {
    private UUID id;
    private String nome;
    private String tipo;
    private String detalhes;
    private BigDecimal peso;
    private BigDecimal precoCompra;
    private BigDecimal precoVenda;
    private LocalDateTime dataCadastro;
    private Dimensoes dimensoes;

    public Produto() {
    }

    public Produto(UUID id, String nome, String tipo, String detalhes, BigDecimal peso, BigDecimal precoCompra, BigDecimal precoVenda, LocalDateTime dataCadastro, Dimensoes dimensoes) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.detalhes = detalhes;
        this.peso = peso;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
        this.dataCadastro = dataCadastro;
        this.dimensoes = dimensoes;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
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

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Dimensoes getDimensoes() {
        return dimensoes;
    }

    public void setDimensoes(Dimensoes dimensoes) {
        this.dimensoes = dimensoes;
    }
}
