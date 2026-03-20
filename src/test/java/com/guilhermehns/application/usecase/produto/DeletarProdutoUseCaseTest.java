package com.guilhermehns.application.usecase.produto;

import com.guilhermehns.application.exception.ProdutoNaoEncontradoException;
import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeletarProdutoUseCaseTest {

    @Test
    void deveDeletarProdutoComSucesso() {
        ProdutoRepository repository = Mockito.mock(ProdutoRepository.class);

        DeletarProdutoUseCase useCase = new DeletarProdutoUseCase(repository);

        UUID id = UUID.randomUUID();

        Produto produto = new Produto();
        produto.setId(id);

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(produto));

        useCase.executar(id);

        Mockito.verify(repository).deleteById(id);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoForEncontradoAoDeletar() {
        ProdutoRepository repository = Mockito.mock(ProdutoRepository.class);
        DeletarProdutoUseCase useCase = new DeletarProdutoUseCase(repository);

        UUID id = UUID.randomUUID();

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.empty());

        ProdutoNaoEncontradoException exception = assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> useCase.executar(id)
        );

        assertEquals("Produto não encontrado.", exception.getMessage());
        Mockito.verify(repository).findById(id);
        Mockito.verify(repository, Mockito.never()).deleteById(Mockito.any());
    }
}
