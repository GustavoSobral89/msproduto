package br.com.techchallenge4.msproduto;

import br.com.techchallenge4.msproduto.controller.ProdutoController;
import br.com.techchallenge4.msproduto.exception.ProdutoNaoEncontradoException;
import br.com.techchallenge4.msproduto.model.Produto;
import br.com.techchallenge4.msproduto.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProdutoControllerTest {

    @InjectMocks
    private ProdutoController produtoController;

    @Mock
    private ProdutoService produtoService;

    private MockMvc mockMvc;

    private Produto produto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(produtoController).build();

        // Setup a mock produto
        produto = new Produto(1L, "Produto Teste", "Descrição Teste", BigDecimal.valueOf(99.99), 10, LocalDateTime.now());
    }

    @Test
    public void testCreateProduto() throws Exception {
        Mockito.when(produtoService.create(any(Produto.class))).thenReturn(produto);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Produto Teste\", \"descricao\":\"Descrição Teste\", \"preco\":99.99, \"quantidadeestoque\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Produto Teste"))
                .andExpect(jsonPath("$.descricao").value("Descrição Teste"))
                .andExpect(jsonPath("$.preco").value(99.99))
                .andExpect(jsonPath("$.quantidadeestoque").value(10));
    }

    @Test
    public void testGetProdutoById() throws Exception {
        Mockito.when(produtoService.getById(anyLong())).thenReturn(produto);

        mockMvc.perform(get("/produtos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Produto Teste"));
    }

    @Test
    public void testGetProdutoByIdNotFound() throws Exception {
        Mockito.when(produtoService.getById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/produtos/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllProdutos() throws Exception {
        List<Produto> produtos = Arrays.asList(produto);
        Mockito.when(produtoService.getAll()).thenReturn(produtos);

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome").value("Produto Teste"));
    }

    @Test
    public void testUpdateProduto() throws Exception {
        Mockito.when(produtoService.update(anyLong(), any(Produto.class))).thenReturn(produto);

        mockMvc.perform(put("/produtos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Produto Teste Atualizado\", \"descricao\":\"Descrição Atualizada\", \"preco\":120.50, \"quantidadeestoque\":20}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Produto Teste"))
                .andExpect(jsonPath("$.descricao").value("Descrição Teste"));
    }

    @Test
    public void testUpdateProdutoNotFound() throws Exception {
        Mockito.when(produtoService.update(anyLong(), any(Produto.class))).thenReturn(null);

        mockMvc.perform(put("/produtos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Produto Teste Atualizado\", \"descricao\":\"Descrição Atualizada\", \"preco\":120.50, \"quantidadeestoque\":20}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteProduto() throws Exception {
        Mockito.doNothing().when(produtoService).delete(anyLong());

        mockMvc.perform(delete("/produtos/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteProdutoNotFound() throws Exception {
        Mockito.doThrow(new ProdutoNaoEncontradoException("Produto não encontrado")).when(produtoService).delete(anyLong());

        mockMvc.perform(delete("/produtos/{id}", 1L))
                .andExpect(status().isNotFound())  // Espera o status 404
                .andExpect(content().string("Produto não encontrado"));  // Verifica se a mensagem da exceção está sendo retornada
    }

    @Test
    public void testVerificarEstoqueSuccess() throws Exception {
        Mockito.when(produtoService.verificarEstoque(anyLong(), anyInt())).thenReturn(true);

        mockMvc.perform(put("/produtos/verificar-estoque/{id}/quantidade/{quantidade}", 1L, 5))
                .andExpect(status().isOk())
                .andExpect(content().string("Estoque atualizado com sucesso."));
    }

    @Test
    public void testVerificarEstoqueFailure() throws Exception {
        Mockito.when(produtoService.verificarEstoque(anyLong(), anyInt())).thenReturn(false);

        mockMvc.perform(put("/produtos/verificar-estoque/{id}/quantidade/{quantidade}", 1L, 15))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Quantidade insuficiente em estoque."));
    }
}