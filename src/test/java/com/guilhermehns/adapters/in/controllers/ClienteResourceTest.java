package com.guilhermehns.adapters.in.controllers;

import com.guilhermehns.application.usecase.cliente.AtualizarClienteUseCase;
import com.guilhermehns.application.usecase.cliente.BuscarClientePorIdUseCase;
import com.guilhermehns.application.usecase.cliente.CriarClienteUseCase;
import com.guilhermehns.application.usecase.cliente.DeletarClienteUseCase;
import com.guilhermehns.application.usecase.cliente.ListarClientesUseCase;
import com.guilhermehns.domain.model.cliente.Cliente;
import com.guilhermehns.domain.model.cliente.Endereco;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
class ClienteResourceTest {

    @InjectMock
    CriarClienteUseCase criarClienteUseCase;

    @InjectMock
    ListarClientesUseCase listarClientesUseCase;

    @InjectMock
    BuscarClientePorIdUseCase buscarClientePorIdUseCase;

    @InjectMock
    AtualizarClienteUseCase atualizarClienteUseCase;

    @InjectMock
    DeletarClienteUseCase deletarClienteUseCase;

    @Test
    void deveListarClientesComSucesso() {
        Cliente cliente = criarClienteValido();

        Mockito.when(listarClientesUseCase.executar())
                .thenReturn(List.of(cliente));

        given()
                .when()
                .get("/clientes")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].nomeCompleto", equalTo("João Silva"))
                .body("[0].cpf", equalTo("12345678900"));
    }

    @Test
    void deveBuscarClientePorIdComSucesso() {
        UUID clienteId = UUID.randomUUID();
        Cliente cliente = criarClienteValido();
        cliente.setId(clienteId);

        Mockito.when(buscarClientePorIdUseCase.executar(clienteId))
                .thenReturn(cliente);

        given()
                .when()
                .get("/clientes/{id}", clienteId)
                .then()
                .statusCode(200)
                .body("id", equalTo(clienteId.toString()))
                .body("nomeCompleto", equalTo("João Silva"))
                .body("email", equalTo("joao@email.com"));
    }

    @Test
    void deveCriarClienteComSucesso() {
        Cliente cliente = criarClienteValido();
        Cliente clienteCriado = criarClienteValido();
        UUID clienteId = UUID.randomUUID();
        clienteCriado.setId(clienteId);

        Mockito.when(criarClienteUseCase.executar(Mockito.any(Cliente.class)))
                .thenReturn(clienteCriado);

        given()
                .contentType(ContentType.JSON)
                .body(cliente)
                .when()
                .post("/clientes")
                .then()
                .statusCode(201)
                .body("id", equalTo(clienteId.toString()))
                .body("nomeCompleto", equalTo("João Silva"));
    }

    @Test
    void deveAtualizarClienteComSucesso() {
        UUID clienteId = UUID.randomUUID();
        Cliente cliente = criarClienteValido();
        Cliente clienteAtualizado = criarClienteValido();
        clienteAtualizado.setId(clienteId);
        clienteAtualizado.setNomeCompleto("João Atualizado");

        Mockito.when(atualizarClienteUseCase.executar(Mockito.eq(clienteId), Mockito.any(Cliente.class)))
                .thenReturn(clienteAtualizado);

        given()
                .contentType(ContentType.JSON)
                .body(cliente)
                .when()
                .put("/clientes/{id}", clienteId)
                .then()
                .statusCode(200)
                .body("id", equalTo(clienteId.toString()))
                .body("nomeCompleto", equalTo("João Atualizado"));
    }

    @Test
    void deveDeletarClienteComSucesso() {
        UUID clienteId = UUID.randomUUID();

        Mockito.doNothing().when(deletarClienteUseCase).executar(clienteId);

        given()
                .when()
                .delete("/clientes/{id}", clienteId)
                .then()
                .statusCode(204);
    }

    private Cliente criarClienteValido() {
        Cliente cliente = new Cliente();
        cliente.setId(UUID.randomUUID());
        cliente.setNomeCompleto("João Silva");
        cliente.setNomeMae("Maria Souza");
        cliente.setCpf("12345678900");
        cliente.setRg("123456789");
        cliente.setEmail("joao@email.com");
        cliente.setTelefone("19999999999");
        cliente.setDataNascimento(LocalDate.of(2000, 1, 1));
        cliente.setDataCadastro(LocalDateTime.of(2026, 3, 20, 10, 0));

        Endereco endereco = new Endereco();
        endereco.setCep("13400-000");
        endereco.setLogradouro("Rua A");
        endereco.setNumero("123");
        endereco.setComplemento("Casa");
        endereco.setBairro("Centro");
        endereco.setCidade("Limeira");
        endereco.setEstado("SP");

        cliente.setEndereco(endereco);
        return cliente;
    }
}