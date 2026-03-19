package com.guilhermehns.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class EncalhadoDTO {
    private UUID produtoId;
    private String nomeProduto;
    private LocalDateTime dataCadastro;

    public EncalhadoDTO() {
    }

    public EncalhadoDTO(UUID produtoId, String nomeProduto, LocalDateTime dataCadastro) {
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
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

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}