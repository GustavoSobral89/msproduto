package br.com.techchallenge4.msproduto.controller;

import br.com.techchallenge4.msproduto.exception.ProdutoNaoEncontradoException;
import br.com.techchallenge4.msproduto.model.Produto;
import br.com.techchallenge4.msproduto.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Operation(summary = "Cria um novo produto", description = "Cria um produto no catálogo com informações como nome, descrição, preço e quantidade em estoque.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @PostMapping
    public ResponseEntity<Produto> create(@RequestBody Produto produto) {
        Produto createdProduto = produtoService.create(produto);
        return ResponseEntity.ok(createdProduto);
    }

    @Operation(summary = "Obtém um produto pelo ID", description = "Recupera os detalhes de um produto baseado no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Produto> getById(@PathVariable Long id) {
        Produto produto = produtoService.getById(id);
        if (produto == null) {
            throw new ProdutoNaoEncontradoException("Produto não encontrado!");
        }
        return ResponseEntity.ok(produto);
    }

    @Operation(summary = "Obtém todos os produtos", description = "Recupera a lista de todos os produtos cadastrados no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar produtos")
    })
    @GetMapping
    public ResponseEntity<List<Produto>> getAll() {
        List<Produto> produtos = produtoService.getAll();
        return ResponseEntity.ok(produtos);
    }

    @Operation(summary = "Atualiza um produto", description = "Atualiza as informações de um produto existente, com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Produto> update(@PathVariable Long id, @RequestBody Produto produto) {
        Produto updatedProduto = produtoService.update(id, produto);
        if (updatedProduto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedProduto);
    }

    // Método de tratamento para a exceção ProdutoNaoEncontradoException
    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<String> handleProdutoNaoEncontradoException(ProdutoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @Operation(summary = "Deleta um produto", description = "Deleta o produto baseado no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        produtoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/verificar-estoque/{id}/quantidade/{quantidade}")
    public ResponseEntity<String> verificarEstoque(@PathVariable Long id, @PathVariable int quantidade) {
        if (produtoService.verificarEstoque(id, quantidade)) {
            produtoService.darBaixaNoEstoque(id, quantidade);
            return ResponseEntity.ok("Estoque atualizado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantidade insuficiente em estoque.");
        }
    }
}
