package br.com.techchallenge4.msproduto.exception;

public class ProdutoNaoEncontradoException extends RuntimeException {

    // Construtor padrão
    public ProdutoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    // Construtor com causa
    public ProdutoNaoEncontradoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}