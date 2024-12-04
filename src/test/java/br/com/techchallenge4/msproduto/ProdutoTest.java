package br.com.techchallenge4.msproduto;

import br.com.techchallenge4.msproduto.model.Produto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ProdutoTest {

    @Test
    public void testProdutoConstructorAndGetters() {
        // Dados para o teste
        String nome = "Produto A";
        String descricao = "Descrição do Produto A";
        BigDecimal preco = new BigDecimal("100.50");
        int quantidadeEstoque = 10;

        // Criação do objeto Produto usando o construtor parametrizado
        Produto produto = new Produto(nome, descricao, preco, quantidadeEstoque);

        // Verificar se os dados foram corretamente atribuídos
        assertEquals(nome, produto.getNome());
        assertEquals(descricao, produto.getDescricao());
        assertEquals(preco, produto.getPreco());
        assertEquals(quantidadeEstoque, produto.getQuantidadeestoque());
    }

    @Test
    public void testProdutoNoArgsConstructor() {
        Produto produto = new Produto();
        assertNull(produto.getNome());
        assertNull(produto.getDescricao());
        assertNull(produto.getPreco());
        assertEquals(0, produto.getQuantidadeestoque());
    }

    @Test
    public void testEqualsAndHashCode() {
        Produto produto1 = new Produto(1L, "Produto A", "Descrição A", new BigDecimal("10.00"), 10, LocalDateTime.now());
        Produto produto2 = new Produto(1L, "Produto A", "Descrição A", new BigDecimal("10.00"), 10, LocalDateTime.now());
        Produto produto3 = new Produto(2L, "Produto B", "Descrição B", new BigDecimal("20.00"), 5, LocalDateTime.now());

        // Teste de igualdade
        assertEquals(produto1, produto2);  // Produto com o mesmo id deve ser igual
        assertNotEquals(produto1, produto3);  // Produto com ids diferentes deve ser diferente

        // Teste de hashCode
        assertEquals(produto1.hashCode(), produto2.hashCode());  // Mesmo id, mesmo hashCode
        assertNotEquals(produto1.hashCode(), produto3.hashCode());  // IDs diferentes, hashCodes diferentes
    }

    @Test
    public void testSetters() {
        Produto produto = new Produto();

        // Definir valores usando os setters
        produto.setNome("Produto B");
        produto.setDescricao("Descrição do Produto B");
        produto.setPreco(new BigDecimal("200.00"));
        produto.setQuantidadeestoque(20);

        // Verificar se os setters funcionaram corretamente
        assertEquals("Produto B", produto.getNome());
        assertEquals("Descrição do Produto B", produto.getDescricao());
        assertEquals(new BigDecimal("200.00"), produto.getPreco());
        assertEquals(20, produto.getQuantidadeestoque());
    }
}
