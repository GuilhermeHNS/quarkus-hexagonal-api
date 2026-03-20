package com.guilhermehns.application.util;

import com.guilhermehns.domain.model.cliente.Cliente;
import com.guilhermehns.domain.model.cliente.Endereco;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClienteValidationHelperTest {

    @Test
    void deveValidarClienteComSucesso() {
        assertDoesNotThrow(() -> ClienteValidationHelper.validarCliente(criarClienteValido()));
    }

    @Test
    void deveLancarExcecaoQuandoClienteForNulo() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> ClienteValidationHelper.validarCliente(null)
        );

        assertEquals("Cliente é obrigatório", excecao.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoDataNascimentoForNula() {
        Cliente cliente = criarClienteValido();
        cliente.setDataNascimento(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> ClienteValidationHelper.validarCliente(cliente)
        );

        assertEquals("Data de nascimento é obrigatória", excecao.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoEnderecoForNulo() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> ClienteValidationHelper.validarEndereco(null)
        );

        assertEquals("Endereço é obrigatório", excecao.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoCampoObrigatorioForVazio() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> ClienteValidationHelper.validarCampoObrigatorio(" ", "Campo obrigatório")
        );

        assertEquals("Campo obrigatório", excecao.getMessage());
    }

    private Cliente criarClienteValido() {
        Cliente cliente = new Cliente();
        cliente.setNomeCompleto("João");
        cliente.setNomeMae("Maria");
        cliente.setCpf("123");
        cliente.setRg("456");
        cliente.setEmail("a@a.com");
        cliente.setTelefone("999");
        cliente.setDataNascimento(LocalDate.of(2000, 1, 1));

        Endereco endereco = new Endereco();
        endereco.setCep("13400-000");
        endereco.setLogradouro("Rua A");
        endereco.setNumero("1");
        endereco.setBairro("Centro");
        endereco.setCidade("Limeira");
        endereco.setEstado("SP");

        cliente.setEndereco(endereco);
        return cliente;
    }
}