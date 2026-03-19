package com.guilhermehns.adapters.out.persistence.mongo.venda;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@MongoEntity(collection = "vendas")
public class VendaEntity {

    public ObjectId id;

    public String vendaId;
    public String clienteId;
    public String codigoVendedor;
    public LocalDateTime dataVenda;
    public String formaPagamento;
    public String numeroCartao;
    public BigDecimal valorPago;
    public List<ItemVendaEntity> itens;
}
