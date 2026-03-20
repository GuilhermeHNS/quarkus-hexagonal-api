package com.guilhermehns.application.usecase.produto;

import com.guilhermehns.application.exception.ProdutoNaoEncontradoException;
import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BuscarProdutoPorIdUseCaseTest {

    @Test
    void deveBuscarProdutoPorIdComSucesso() {
        ProdutoRepository repository = Mockito.mock(ProdutoRepository.class);

        BuscarProdutoPorIdUseCase useCase = new BuscarProdutoPorIdUseCase(repository);

        UUID id = UUID.randomUUID();

        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome("Produto Teste");

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(produto));

        Produto resultado = useCase.executar(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoForEncontradoAoBuscarPorId() {
        ProdutoRepository repository = Mockito.mock(ProdutoRepository.class);
        BuscarProdutoPorIdUseCase useCase = new BuscarProdutoPorIdUseCase(repository);

        UUID id = UUID.randomUUID();

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.empty());

        ProdutoNaoEncontradoException exception = assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> useCase.executar(id)
        );

        assertEquals("Produto não encontrado.", exception.getMessage());
        Mockito.verify(repository).findById(id);
    }
}
