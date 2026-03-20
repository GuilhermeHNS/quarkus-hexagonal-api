package com.guilhermehns.application.util;

import com.guilhermehns.domain.model.cliente.Cliente;
import com.guilhermehns.domain.model.cliente.Endereco;

public class ClienteValidationHelper {
    public static void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }

        validarCampoObrigatorio(cliente.getNomeCompleto(), "Nome completo é obrigatório");
        validarCampoObrigatorio(cliente.getNomeMae(), "Nome da mãe é obrigatório");
        validarCampoObrigatorio(cliente.getCpf(), "CPF é obrigatório");
        validarCampoObrigatorio(cliente.getRg(), "RG é obrigatório");
        validarCampoObrigatorio(cliente.getEmail(), "Email é obrigatório");
        validarCampoObrigatorio(cliente.getTelefone(), "Telefone é obrigatório");

        if (cliente.getDataNascimento() == null) {
            throw new IllegalArgumentException("Data de nascimento é obrigatória");
        }

        validarEndereco(cliente.getEndereco());
    }

    public static void validarCampoObrigatorio(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
    }

    public static void validarEndereco(Endereco endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço é obrigatório");
        }

        validarCampoObrigatorio(endereco.getCep(), "CEP é obrigatório");
        validarCampoObrigatorio(endereco.getLogradouro(), "Logradouro é obrigatório");
        validarCampoObrigatorio(endereco.getNumero(), "Número é obrigatório");
        validarCampoObrigatorio(endereco.getBairro(), "Bairro é obrigatório");
        validarCampoObrigatorio(endereco.getCidade(), "Cidade é obrigatória");
        validarCampoObrigatorio(endereco.getEstado(), "Estado é obrigatório");
    }
}
