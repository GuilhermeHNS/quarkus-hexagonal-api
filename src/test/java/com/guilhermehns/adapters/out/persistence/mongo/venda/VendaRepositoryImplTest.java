package com.guilhermehns.adapters.out.persistence.mongo.venda;

import com.guilhermehns.application.dto.ItemMaiorFaturamentoDTO;
import com.guilhermehns.application.dto.FaturamentoMensalItemDTO;
import com.guilhermehns.domain.model.cliente.Cliente;
import com.guilhermehns.domain.model.cliente.Endereco;
import com.guilhermehns.domain.model.produto.Dimensoes;
import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.model.venda.ItemVenda;
import com.guilhermehns.domain.model.venda.Venda;
import com.guilhermehns.adapters.out.persistence.mongo.cliente.ClienteRepositoryImpl;
import com.guilhermehns.adapters.out.persistence.mongo.produto.ProdutoRepositoryImpl;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class VendaRepositoryImplTest {

    @Inject
    VendaRepositoryImpl vendaRepository;

    @Inject
    ClienteRepositoryImpl clienteRepository;

    @Inject
    ProdutoRepositoryImpl produtoRepository;

    @BeforeEach
    void setUp() {
        vendaRepository.deleteAll();
        clienteRepository.deleteAll();
        produtoRepository.deleteAll();
    }

    @Test
    void deveSalvarVendaComSucesso() {
        Cliente cliente = clienteRepository.save(criarClienteValido());
        Produto produto = produtoRepository.save(criarProdutoValido("Notebook", "2500.00", "3500.00"));

        Venda venda = criarVendaValida(cliente, produto, 2, "100.00");

        Venda resultado = vendaRepository.save(venda);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("VEND001", resultado.getCodigoVendedor());
        assertEquals("DINHEIRO", resultado.getFormaPagamento());
        assertEquals(new BigDecimal("200.00"), resultado.getValorPago());
        assertNotNull(resultado.getItens());
        assertEquals(1, resultado.getItens().size());
        assertEquals(2, resultado.getItens().get(0).getQuantidade());
        assertEquals(new BigDecimal("100.00"), resultado.getItens().get(0).getValorUnitario());
        assertNotNull(resultado.getItens().get(0).getProduto());
    }

    @Test
    void deveBuscarVendaPorIdComSucesso() {
        Cliente cliente = clienteRepository.save(criarClienteValido());
        Produto produto = produtoRepository.save(criarProdutoValido("Notebook", "2500.00", "3500.00"));
        Venda venda = vendaRepository.save(criarVendaValida(cliente, produto, 2, "100.00"));

        Optional<Venda> resultado = vendaRepository.findById(venda.getId());

        assertTrue(resultado.isPresent());
        assertEquals(venda.getId(), resultado.get().getId());
        assertEquals("VEND001", resultado.get().getCodigoVendedor());
        assertNotNull(resultado.get().getCliente());
        assertEquals(cliente.getId(), resultado.get().getCliente().getId());
        assertNotNull(resultado.get().getItens());
        assertEquals(1, resultado.get().getItens().size());
        assertEquals(produto.getId(), resultado.get().getItens().get(0).getProduto().getId());
        assertEquals(2, resultado.get().getItens().get(0).getQuantidade());
        assertEquals(new BigDecimal("100.00"), resultado.get().getItens().get(0).getValorUnitario());
    }

    @Test
    void deveRetornarVazioQuandoVendaNaoExistir() {
        Optional<Venda> resultado = vendaRepository.findById(UUID.randomUUID());

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveListarTodasAsVendas() {
        Cliente cliente = clienteRepository.save(criarClienteValido());
        Produto produto1 = produtoRepository.save(criarProdutoValido("Notebook", "2500.00", "3500.00"));
        Produto produto2 = produtoRepository.save(criarProdutoValido("Mouse", "50.00", "90.00"));

        vendaRepository.save(criarVendaValida(cliente, produto1, 2, "100.00"));
        vendaRepository.save(criarVendaValida(cliente, produto2, 1, "50.00"));

        List<Venda> resultado = vendaRepository.findAllVendas();

        assertEquals(2, resultado.size());
    }

    @Test
    void deveDeletarVendaComSucesso() {
        Cliente cliente = clienteRepository.save(criarClienteValido());
        Produto produto = produtoRepository.save(criarProdutoValido("Notebook", "2500.00", "3500.00"));
        Venda venda = vendaRepository.save(criarVendaValida(cliente, produto, 2, "100.00"));

        vendaRepository.deleteById(venda.getId());

        Optional<Venda> resultado = vendaRepository.findById(venda.getId());

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveBuscarProdutosComMaiorFaturamentoComSucesso() {
        Cliente cliente = clienteRepository.save(criarClienteValido());

        Produto produto1 = produtoRepository.save(criarProdutoValido("Produto 1", "100.00", "150.00"));
        Produto produto2 = produtoRepository.save(criarProdutoValido("Produto 2", "200.00", "300.00"));
        Produto produto3 = produtoRepository.save(criarProdutoValido("Produto 3", "300.00", "400.00"));
        Produto produto4 = produtoRepository.save(criarProdutoValido("Produto 4", "400.00", "500.00"));
        Produto produto5 = produtoRepository.save(criarProdutoValido("Produto 5", "500.00", "600.00"));

        vendaRepository.save(criarVendaValida(cliente, produto1, 1, "100.00")); // 100
        vendaRepository.save(criarVendaValida(cliente, produto2, 2, "200.00")); // 400
        vendaRepository.save(criarVendaValida(cliente, produto3, 3, "300.00")); // 900
        vendaRepository.save(criarVendaValida(cliente, produto4, 4, "400.00")); // 1600
        vendaRepository.save(criarVendaValida(cliente, produto5, 5, "500.00")); // 2500

        List<ItemMaiorFaturamentoDTO> resultado = vendaRepository.buscarProdutosComMaiorFaturamento();

        assertEquals(4, resultado.size());
        assertEquals("Produto 5", resultado.get(0).getNomeProduto());
        assertEquals("Produto 4", resultado.get(1).getNomeProduto());
        assertEquals("Produto 3", resultado.get(2).getNomeProduto());
        assertEquals("Produto 2", resultado.get(3).getNomeProduto());
    }

    @Test
    void deveBuscarFaturamentoMensalComSucesso() {
        Cliente cliente = clienteRepository.save(criarClienteValido());
        Produto produto = produtoRepository.save(criarProdutoValido("Notebook", "2500.00", "3500.00"));

        Venda vendaMarco = criarVendaValida(cliente, produto, 2, "100.00");
        vendaMarco.setDataVenda(LocalDateTime.of(2026, 3, 10, 10, 0));

        Venda vendaJaneiro = criarVendaValida(cliente, produto, 1, "200.00");
        vendaJaneiro.setId(UUID.randomUUID());
        vendaJaneiro.setDataVenda(LocalDateTime.of(2026, 1, 15, 10, 0));

        vendaRepository.save(vendaMarco);
        vendaRepository.save(vendaJaneiro);

        List<FaturamentoMensalItemDTO> resultado = vendaRepository.buscaFaturamentoMensal(LocalDate.of(2026, 3, 20));

        assertEquals(2, resultado.size());

        assertEquals(2026, resultado.get(0).getAno());
        assertEquals(1, resultado.get(0).getMes());
        assertEquals(new BigDecimal("200.00"), resultado.get(0).getFaturamento());

        assertEquals(2026, resultado.get(1).getAno());
        assertEquals(3, resultado.get(1).getMes());
        assertEquals(new BigDecimal("200.00"), resultado.get(1).getFaturamento());
    }

    private Venda criarVendaValida(Cliente cliente, Produto produto, Integer quantidade, String valorUnitario) {
        ItemVenda item = new ItemVenda();
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setValorUnitario(new BigDecimal(valorUnitario));

        Venda venda = new Venda();
        venda.setId(UUID.randomUUID());
        venda.setCliente(cliente);
        venda.setCodigoVendedor("VEND001");
        venda.setDataVenda(LocalDateTime.of(2026, 3, 20, 10, 0));
        venda.setFormaPagamento("DINHEIRO");
        venda.setValorPago(new BigDecimal("200.00"));
        venda.setItens(List.of(item));

        return venda;
    }

    private Cliente criarClienteValido() {
        Cliente cliente = new Cliente();
        cliente.setId(UUID.randomUUID());
        cliente.setNomeCompleto("João Silva");
        cliente.setNomeMae("Maria Souza");
        cliente.setCpf("12345678900");
        cliente.setRg("123456789");
        cliente.setEmail("joao@email.com");
        cliente.setTelefone("19999999999");
        cliente.setDataNascimento(LocalDate.of(2000, 1, 1));
        cliente.setDataCadastro(LocalDateTime.of(2026, 3, 20, 10, 0));

        Endereco endereco = new Endereco();
        endereco.setCep("13400-000");
        endereco.setLogradouro("Rua A");
        endereco.setNumero("123");
        endereco.setComplemento("Casa");
        endereco.setBairro("Centro");
        endereco.setCidade("Limeira");
        endereco.setEstado("SP");

        cliente.setEndereco(endereco);
        return cliente;
    }

    private Produto criarProdutoValido(String nome, String precoCompra, String precoVenda) {
        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome(nome);
        produto.setTipo("Eletrônico");
        produto.setDetalhes("Produto de teste");
        produto.setPeso(new BigDecimal("2.50"));
        produto.setPrecoCompra(new BigDecimal(precoCompra));
        produto.setPrecoVenda(new BigDecimal(precoVenda));
        produto.setDataCadastro(LocalDateTime.of(2026, 3, 20, 10, 0));

        Dimensoes dimensoes = new Dimensoes();
        dimensoes.setAltura(new BigDecimal("2.00"));
        dimensoes.setLargura(new BigDecimal("35.00"));
        dimensoes.setProfundidade(new BigDecimal("25.00"));

        produto.setDimensoes(dimensoes);
        return produto;
    }
}