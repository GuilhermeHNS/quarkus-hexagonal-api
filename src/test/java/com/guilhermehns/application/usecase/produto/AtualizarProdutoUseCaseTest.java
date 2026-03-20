package com.guilhermehns.application.usecase.produto;

import com.guilhermehns.application.exception.ProdutoNaoEncontradoException;
import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AtualizarProdutoUseCaseTest {

    @Test
    void deveAtualizarProdutoComSucesso() {

        ProdutoRepository repository = Mockito.mock(ProdutoRepository.class);

        AtualizarProdutoUseCase useCase = new AtualizarProdutoUseCase(repository);

        UUID id = UUID.randomUUID();

        Produto existente = new Produto();
        existente.setId(id);
        existente.setNome("Produto Antigo");

        Produto novosDados = new Produto();
        novosDados.setNome("Produto Novo");
        novosDados.setTipo("Tipo Novo");
        novosDados.setPrecoCompra(new BigDecimal("100"));
        novosDados.setPrecoVenda(new BigDecimal("200"));

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(existente));

        Mockito.when(repository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Produto resultado = useCase.executar(id, novosDados);

        assertEquals(id, resultado.getId());
        assertEquals("Produto Novo", resultado.getNome());
        assertEquals("Tipo Novo", resultado.getTipo());
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

