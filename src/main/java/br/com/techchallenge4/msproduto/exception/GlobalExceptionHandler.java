package br.com.techchallenge4.msproduto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<String> handleProdutoNaoExistenteException(ProdutoNaoEncontradoException ex) {
        // Retorna a mensagem de erro com o status HTTP 404 (Not Found)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}