package com.guilhermehns.application.util;

import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.model.venda.ItemVenda;
import com.guilhermehns.domain.model.venda.Venda;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VendaCalculationHelperTest {

    @Test
    void deveCalcularTotaisComSucesso() {
        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());

        ItemVenda item1 = new ItemVenda();
        item1.setProduto(produto);
        item1.setQuantidade(2);
        item1.setValorUnitario(new BigDecimal("100.00"));

        ItemVenda item2 = new ItemVenda();
        item2.setProduto(produto);
        item2.setQuantidade(1);
        item2.setValorUnitario(new BigDecimal("50.00"));

        Venda venda = new Venda();
        venda.setItens(List.of(item1, item2));

        VendaCalculationHelper.calcularTotais(venda);

        assertEquals(new BigDecimal("250.00"), venda.getValorTotalProdutos());
        assertEquals(new BigDecimal("22.50"), venda.getImposto());
        assertEquals(new BigDecimal("272.50"), venda.getValorTotalVenda());
    }

    @Test
    void deveRetornarTotaisZeradosQuandoVendaNaoPossuirItens() {
        Venda venda = new Venda();
        venda.setItens(List.of());

        VendaCalculationHelper.calcularTotais(venda);

        assertEquals(new BigDecimal("0.00"), venda.getValorTotalProdutos());
        assertEquals(new BigDecimal("0.00"), venda.getImposto());
        assertEquals(new BigDecimal("0.00"), venda.getValorTotalVenda());
    }

    @Test
    void deveRetornarTotaisZeradosQuandoItensForemNulos() {
        Venda venda = new Venda();
        venda.setItens(null);

        VendaCalculationHelper.calcularTotais(venda);

        assertEquals(new BigDecimal("0.00"), venda.getValorTotalProdutos());
        assertEquals(new BigDecimal("0.00"), venda.getImposto());
        assertEquals(new BigDecimal("0.00"), venda.getValorTotalVenda());
    }
}