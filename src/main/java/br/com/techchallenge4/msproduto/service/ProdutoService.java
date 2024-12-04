package br.com.techchallenge4.msproduto.service;

import br.com.techchallenge4.msproduto.exception.ProdutoNaoEncontradoException;
import br.com.techchallenge4.msproduto.model.Produto;
import br.com.techchallenge4.msproduto.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    // Criar um novo produto
    public Produto create(Produto produto) {
        produto.setCreatedatetime(LocalDateTime.now());
        return produtoRepository.save(produto);
    }

    // Obter um produto pelo ID
    public Produto getById(Long id) {
        return produtoRepository.findById(id).orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado!"));
    }

    // Obter todos os produtos
    public List<Produto> getAll() {
        return produtoRepository.findAll();
    }

    // Atualizar um produto
    public Produto update(Long id, Produto produto) {
        if (!produtoRepository.existsById(id)) {
            return null;  // Retorna null se não encontrar o produto
        }
        produto.setId(id);
        return produtoRepository.save(produto);
    }

    public boolean verificarEstoque(Long id, int quantidade) {
        Optional<Produto> produto = produtoRepository.findById(id);
        return produto.isPresent() && produto.get().getQuantidadeestoque() >= quantidade;
    }

    public void darBaixaNoEstoque(Long id, int quantidade) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado!"));
        if (produto.getQuantidadeestoque() >= quantidade) {
            produto.setQuantidadeestoque(produto.getQuantidadeestoque() - quantidade);
            produtoRepository.save(produto);
        }
    }

    // Método para deletar um produto
    public void delete(Long id) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não existe para ser deletado!"));
        produtoRepository.delete(produto); // Deleta o produto
    }
}
