package com.guilhermehns.domain.model.produto;

import java.math.BigDecimal;

public class Dimensoes {
    private BigDecimal altura;
    private BigDecimal largura;
    private BigDecimal profundidade;

    public Dimensoes(BigDecimal altura, BigDecimal largura, BigDecimal profundidade) {
        this.altura = altura;
        this.largura = largura;
        this.profundidade = profundidade;
    }

    public Dimensoes() {
    }

    public BigDecimal getAltura() {
        return altura;
    }

    public void setAltura(BigDecimal altura) {
        this.altura = altura;
    }

    public BigDecimal getLargura() {
        return largura;
    }

    public void setLargura(BigDecimal largura) {
        this.largura = largura;
    }

    public BigDecimal getProfundidade() {
        return profundidade;
    }

    public void setProfundidade(BigDecimal profundidade) {
        this.profundidade = profundidade;
    }
}
