package com.guilhermehns.application.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class NovoClienteDTO {
    private UUID clienteId;
    private String nomeCompleto;
    private LocalDate dataNascimento;
    private LocalDateTime dataCadastro;

    public NovoClienteDTO() {
    }

    public NovoClienteDTO(UUID clienteId, String nomeCompleto, LocalDate dataNascimento, LocalDateTime dataCadastro) {
        this.clienteId = clienteId;
        this.nomeCompleto = nomeCompleto;
        this.dataNascimento = dataNascimento;
        this.dataCadastro = dataCadastro;
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public void setClienteId(UUID clienteId) {
        this.clienteId = clienteId;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}