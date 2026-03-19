package com.guilhermehns.adapters.out.persistence.mongo.cliente;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalDateTime;

@MongoEntity(collection = "clientes")
public class ClienteEntity {

    public ObjectId id;

    public String clienteId;
    public String nomeCompleto;
    public String nomeMae;
    public String cpf;
    public String rg;
    public String email;
    public String telefone;
    public LocalDate dataNascimento;
    public LocalDateTime dataCadastro;

    public EnderecoEntity endereco;
}
