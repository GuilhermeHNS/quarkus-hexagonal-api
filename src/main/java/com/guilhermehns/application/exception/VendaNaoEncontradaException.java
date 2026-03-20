package com.guilhermehns.application.exception;

public class VendaNaoEncontradaException extends RuntimeException{

    public VendaNaoEncontradaException() {
        super("Venda não encontrada.");
    }
}
