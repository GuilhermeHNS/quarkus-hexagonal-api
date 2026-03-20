package com.guilhermehns.application.util;

import com.guilhermehns.domain.model.venda.Venda;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class VendaCalculationHelper {

    public static Venda calcularTotais(Venda venda) {
        if (venda.getItens() == null || venda.getItens().isEmpty()) {
            venda.setValorTotalProdutos(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            venda.setImposto(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            venda.setValorTotalVenda(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            return venda;
        }

        BigDecimal valorTotalProdutos = venda.getItens().stream()
                .map(item -> item.getValorUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal imposto = valorTotalProdutos
                .multiply(new BigDecimal("0.09"))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal valorTotalVenda = valorTotalProdutos
                .add(imposto)
                .setScale(2, RoundingMode.HALF_UP);

        venda.setValorTotalProdutos(valorTotalProdutos);
        venda.setImposto(imposto);
        venda.setValorTotalVenda(valorTotalVenda);

        return venda;
    }
}
