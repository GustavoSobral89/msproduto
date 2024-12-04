package br.com.techchallenge4.msproduto.model;

import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;

public class ProdutoProcessor implements ItemProcessor<Produto, Produto> {
    @Override
    public Produto process(Produto item) throws Exception {
        item.setCreatedatetime(LocalDateTime.now());
        return item;
    }
}
