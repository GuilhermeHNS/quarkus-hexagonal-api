package com.guilhermehns.application.usecase.produto;

import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListarProdutosUseCaseTest {

    @Test
    void deveListarProdutosComSucesso() {
        ProdutoRepository repository = Mockito.mock(ProdutoRepository.class);

        ListarProdutosUseCase useCase = new ListarProdutosUseCase(repository);

        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome("Produto Teste");

        Mockito.when(repository.findAllProdutos())
                .thenReturn(Collections.singletonList(produto));

        List<Produto> resultado = useCase.executar();

        assertEquals(1, resultado.size());
        assertEquals("Produto Teste", resultado.get(0).getNome());
    }
}
