package com.guilhermehns.adapters.out.persistence.mongo.produto;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@MongoEntity(collection = "produtos")
public class ProdutoEntity {
    public ObjectId id;

    public String produtoId;

    public String nome;
    public String tipo;
    public String detalhes;

    public BigDecimal peso;

    public BigDecimal precoCompra;
    public BigDecimal precoVenda;

    public LocalDateTime dataCadastro;

    public DimensoesEntity dimensoes;

}
