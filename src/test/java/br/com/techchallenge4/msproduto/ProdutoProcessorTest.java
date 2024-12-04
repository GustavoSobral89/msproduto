package br.com.techchallenge4.msproduto;

import br.com.techchallenge4.msproduto.model.Produto;
import br.com.techchallenge4.msproduto.model.ProdutoProcessor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ProdutoProcessorTest {

    @Test
    public void testProcess() throws Exception {
        // Criação de um objeto Produto
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto A");
        produto.setDescricao("Descrição do Produto A");
        produto.setPreco(new BigDecimal("100.50"));
        produto.setQuantidadeestoque(10);

        // Criação de um ProdutoProcessor
        ProdutoProcessor produtoProcessor = new ProdutoProcessor();

        // Processamento do produto
        Produto processedProduto = produtoProcessor.process(produto);

        // Verificar se o campo createdatetime foi atribuído corretamente
        assertNotNull(processedProduto.getCreatedatetime(), "A data de criação não deve ser nula");
        assertTrue(processedProduto.getCreatedatetime().isBefore(LocalDateTime.now().plusSeconds(1)),
                "A data de criação deve ser dentro do intervalo atual");

        // Verificar se o resto dos atributos do produto não foi alterado
        assertEquals(produto.getId(), processedProduto.getId());
        assertEquals(produto.getNome(), processedProduto.getNome());
        assertEquals(produto.getDescricao(), processedProduto.getDescricao());
        assertEquals(produto.getPreco(), processedProduto.getPreco());
        assertEquals(produto.getQuantidadeestoque(), processedProduto.getQuantidadeestoque());
    }
}