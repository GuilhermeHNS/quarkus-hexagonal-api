package com.guilhermehns.application.exception;

public class ProdutoNaoEncontradoException extends RuntimeException{
    public ProdutoNaoEncontradoException() {
        super("Produto não encontrado.");
    }
}
