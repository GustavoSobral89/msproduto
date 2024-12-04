package br.com.techchallenge4.msproduto;

import br.com.techchallenge4.msproduto.exception.ProdutoNaoEncontradoException;
import br.com.techchallenge4.msproduto.model.Produto;
import br.com.techchallenge4.msproduto.repository.ProdutoRepository;
import br.com.techchallenge4.msproduto.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProdutoServiceTest {

    @Autowired
    private ProdutoService produtoService;

    private ProdutoRepository produtoRepositoryMock;

    private Produto produto;

    @BeforeEach
    public void setup() {
        produtoRepositoryMock = Mockito.mock(ProdutoRepository.class);
        produtoService = new ProdutoService(produtoRepositoryMock);

        produto = new Produto(1L, "Produto Teste", "Descrição Teste", new BigDecimal("100.00"), 10, LocalDateTime.now());
    }

    @Test
    public void testCreate() {
        when(produtoRepositoryMock.save(any(Produto.class))).thenReturn(produto);

        Produto result = produtoService.create(produto);

        assertNotNull(result);
        assertEquals(produto.getNome(), result.getNome());
        verify(produtoRepositoryMock, times(1)).save(any(Produto.class));
    }

    @Test
    public void testGetById() {
        when(produtoRepositoryMock.findById(1L)).thenReturn(Optional.of(produto));

        Produto result = produtoService.getById(1L);

        assertNotNull(result);
        assertEquals(produto.getId(), result.getId());
        verify(produtoRepositoryMock, times(1)).findById(1L);
    }

    @Test
    public void testGetByIdThrowsException() {
        when(produtoRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        ProdutoNaoEncontradoException exception = assertThrows(ProdutoNaoEncontradoException.class, () -> {
            produtoService.getById(1L);
        });

        assertEquals("Produto não encontrado!", exception.getMessage());
    }

    @Test
    public void testGetAll() {
        when(produtoRepositoryMock.findAll()).thenReturn(List.of(produto));

        List<Produto> result = produtoService.getAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(produtoRepositoryMock, times(1)).findAll();
    }

    @Test
    public void testUpdate() {
        when(produtoRepositoryMock.existsById(1L)).thenReturn(true);
        when(produtoRepositoryMock.save(any(Produto.class))).thenReturn(produto);

        Produto result = produtoService.update(1L, produto);

        assertNotNull(result);
        assertEquals(produto.getId(), result.getId());
        verify(produtoRepositoryMock, times(1)).save(any(Produto.class));
    }

    @Test
    public void testUpdateProdutoNaoEncontrado() {
        when(produtoRepositoryMock.existsById(1L)).thenReturn(false);

        Produto result = produtoService.update(1L, produto);

        assertNull(result);
        verify(produtoRepositoryMock, times(1)).existsById(1L);
    }

    @Test
    public void testVerificarEstoque() {
        when(produtoRepositoryMock.findById(1L)).thenReturn(Optional.of(produto));

        boolean result = produtoService.verificarEstoque(1L, 5);

        assertTrue(result);
    }

    @Test
    public void testVerificarEstoqueProdutoNaoEncontrado() {
        when(produtoRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        boolean result = produtoService.verificarEstoque(1L, 5);

        assertFalse(result);
    }

    @Test
    public void testDarBaixaNoEstoque() {
        when(produtoRepositoryMock.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepositoryMock.save(any(Produto.class))).thenReturn(produto);

        produtoService.darBaixaNoEstoque(1L, 5);

        assertEquals(5, produto.getQuantidadeestoque());
        verify(produtoRepositoryMock, times(1)).save(any(Produto.class));
    }

    @Test
    public void testDarBaixaNoEstoqueProdutoNaoEncontrado() {
        when(produtoRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        ProdutoNaoEncontradoException exception = assertThrows(ProdutoNaoEncontradoException.class, () -> {
            produtoService.darBaixaNoEstoque(1L, 5);
        });

        assertEquals("Produto não encontrado!", exception.getMessage());
    }

    @Test
    public void testDelete() {
        when(produtoRepositoryMock.findById(1L)).thenReturn(Optional.of(produto));
        doNothing().when(produtoRepositoryMock).delete(any(Produto.class));

        produtoService.delete(1L);

        verify(produtoRepositoryMock, times(1)).delete(any(Produto.class));
    }

    @Test
    public void testDeleteProdutoNaoEncontrado() {
        when(produtoRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        ProdutoNaoEncontradoException exception = assertThrows(ProdutoNaoEncontradoException.class, () -> {
            produtoService.delete(1L);
        });

        assertEquals("Produto não existe para ser deletado!", exception.getMessage());
    }
}