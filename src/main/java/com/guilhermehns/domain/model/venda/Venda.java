package com.guilhermehns.domain.model.venda;

import com.guilhermehns.domain.model.cliente.Cliente;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Venda {
    private UUID id;
    private Cliente cliente;
    private String codigoVendedor;
    private LocalDateTime dataVenda;
    private String formaPagamento;
    private String numeroCartao;
    private BigDecimal valorPago;
    private List<ItemVenda> itens;
    private BigDecimal valorTotalProdutos;
    private BigDecimal valorTotalVenda;
    private BigDecimal imposto;

    public Venda() {
    }

    public Venda(UUID id, Cliente cliente, String codigoVendedor, LocalDateTime dataVenda, String formaPagamento, String numeroCartao, BigDecimal valorPago, List<ItemVenda> itens, BigDecimal valorTotalProdutos, BigDecimal valorTotalVenda, BigDecimal imposto) {
        this.id = id;
        this.cliente = cliente;
        this.codigoVendedor = codigoVendedor;
        this.dataVenda = dataVenda;
        this.formaPagamento = formaPagamento;
        this.numeroCartao = numeroCartao;
        this.valorPago = valorPago;
        this.itens = itens;
        this.valorTotalProdutos = valorTotalProdutos;
        this.valorTotalVenda = valorTotalVenda;
        this.imposto = imposto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getCodigoVendedor() {
        return codigoVendedor;
    }

    public void setCodigoVendedor(String codigoVendedor) {
        this.codigoVendedor = codigoVendedor;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    public BigDecimal getValorTotalProdutos() {
        return valorTotalProdutos;
    }

    public void setValorTotalProdutos(BigDecimal valorTotalProdutos) {
        this.valorTotalProdutos = valorTotalProdutos;
    }

    public BigDecimal getValorTotalVenda() {
        return valorTotalVenda;
    }

    public void setValorTotalVenda(BigDecimal valorTotalVenda) {
        this.valorTotalVenda = valorTotalVenda;
    }

    public BigDecimal getImposto() {
        return imposto;
    }

    public void setImposto(BigDecimal imposto) {
        this.imposto = imposto;
    }
}
