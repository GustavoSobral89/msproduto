package br.com.techchallenge4.msproduto.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int quantidadeestoque;
    private LocalDateTime createdatetime;

    public Produto(String nome, String descricao, BigDecimal preco, int quantidadeestoque) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeestoque = quantidadeestoque;
    }

    // Adicionando equals() e hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return id != null && id.equals(produto.id);  // Considera o ID como critério de igualdade
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;  // Garante que o ID seja utilizado no hash
    }
}