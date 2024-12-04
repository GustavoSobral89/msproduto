package br.com.techchallenge4.msproduto.repository;

import br.com.techchallenge4.msproduto.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
