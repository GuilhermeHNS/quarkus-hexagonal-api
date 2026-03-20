package com.guilhermehns.adapters.out.persistence.mongo.venda;

import com.guilhermehns.domain.model.cliente.Cliente;
import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.model.venda.ItemVenda;
import com.guilhermehns.domain.model.venda.Venda;
import com.guilhermehns.domain.repository.ClienteRepository;
import com.guilhermehns.domain.repository.ProdutoRepository;
import com.guilhermehns.domain.repository.VendaRepository;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class VendaRepositoryImpl implements VendaRepository, PanacheMongoRepository<VendaEntity> {

    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public VendaRepositoryImpl(ClienteRepository clienteRepository, ProdutoRepository produtoRepository) {
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    @Override
    public Venda save(Venda venda) {
        VendaEntity entity = new VendaEntity();

        entity.vendaId = venda.getId().toString();

        if (venda.getId() != null) {
            VendaEntity existingEntity = find("vendaId", venda.getId().toString()).firstResult();
            if (existingEntity != null) {
                entity.id = existingEntity.id;
            }
        }

        entity.clienteId = venda.getCliente() != null && venda.getCliente().getId() != null
                ? venda.getCliente().getId().toString()
                : null;
        entity.codigoVendedor = venda.getCodigoVendedor();
        entity.dataVenda = venda.getDataVenda();
        entity.formaPagamento = venda.getFormaPagamento();
        entity.numeroCartao = venda.getNumeroCartao();
        entity.valorPago = venda.getValorPago();

        if (venda.getItens() != null) {
            entity.itens = venda.getItens().stream().map(item -> {
                ItemVendaEntity itemEntity = new ItemVendaEntity();
                itemEntity.produtoId = item.getProduto() != null && item.getProduto().getId() != null
                        ? item.getProduto().getId().toString()
                        : null;
                itemEntity.quantidade = item.getQuantidade();
                itemEntity.valorUnitario = item.getValorUnitario();
                return itemEntity;
            }).toList();
        }

        persistOrUpdate(entity);
        return venda;
    }

    @Override
    public List<Venda> findAllVendas() {
        return listAll().stream().map(entity -> {
            Venda venda = new Venda();

            venda.setId(UUID.fromString(entity.vendaId));
            venda.setCodigoVendedor(entity.codigoVendedor);
            venda.setDataVenda(entity.dataVenda);
            venda.setFormaPagamento(entity.formaPagamento);
            venda.setNumeroCartao(entity.numeroCartao);
            venda.setValorPago(entity.valorPago);

            if (entity.clienteId != null) {
                Cliente cliente = clienteRepository.findById(UUID.fromString(entity.clienteId))
                        .orElse(null);
                venda.setCliente(cliente);
            }

            if (entity.itens != null) {
                venda.setItens(entity.itens.stream().map(itemEntity -> {
                    ItemVenda item = new ItemVenda();

                    if (itemEntity.produtoId != null) {
                        Produto produto = produtoRepository.findById(UUID.fromString(itemEntity.produtoId))
                                .orElse(null);
                        item.setProduto(produto);
                    }

                    item.setQuantidade(itemEntity.quantidade);
                    item.setValorUnitario(itemEntity.valorUnitario);
                    return item;
                }).toList());
            }

            if (venda.getItens() != null) {
                BigDecimal valorTotalProdutos = venda.getItens().stream()
                        .map(item -> item.getValorUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                venda.setValorTotalProdutos(valorTotalProdutos);
                BigDecimal imposto = valorTotalProdutos
                        .multiply(new BigDecimal("0.09"))
                        .setScale(2, RoundingMode.HALF_UP);

                BigDecimal valorTotalVenda = valorTotalProdutos
                        .add(imposto)
                        .setScale(2, RoundingMode.HALF_UP);

                venda.setImposto(imposto);
                venda.setValorTotalVenda(valorTotalVenda);
            }

            return venda;
        }).toList();
    }

    @Override
    public Optional<Venda> findById(UUID id) {
        VendaEntity entity = find("vendaId", id.toString()).firstResult();
        if(entity == null) {
            return Optional.empty();
        }

        Venda venda = new Venda();
        venda.setId(UUID.fromString(entity.vendaId));
        venda.setCodigoVendedor(entity.codigoVendedor);
        venda.setDataVenda(entity.dataVenda);
        venda.setFormaPagamento(entity.formaPagamento);
        venda.setNumeroCartao(entity.numeroCartao);
        venda.setValorPago(entity.valorPago);

        if (entity.clienteId != null) {
            Cliente cliente = clienteRepository.findById(UUID.fromString(entity.clienteId))
                    .orElse(null);
            venda.setCliente(cliente);
        }

        if (entity.itens != null) {
            venda.setItens(entity.itens.stream().map(itemEntity -> {
                ItemVenda item = new ItemVenda();

                if (itemEntity.produtoId != null) {
                    Produto produto = produtoRepository.findById(UUID.fromString(itemEntity.produtoId))
                            .orElse(null);
                    item.setProduto(produto);
                }

                item.setQuantidade(itemEntity.quantidade);
                item.setValorUnitario(itemEntity.valorUnitario);
                return item;
            }).toList());
        }

        if (venda.getItens() != null) {
            BigDecimal valorTotalProdutos = venda.getItens().stream()
                    .map(item -> item.getValorUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            venda.setValorTotalProdutos(valorTotalProdutos);
            BigDecimal imposto = valorTotalProdutos
                    .multiply(new BigDecimal("0.09"))
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal valorTotalVenda = valorTotalProdutos
                    .add(imposto)
                    .setScale(2, RoundingMode.HALF_UP);

            venda.setImposto(imposto);
            venda.setValorTotalVenda(valorTotalVenda);

        }

        return Optional.of(venda);
    }

    @Override
    public void deleteById(UUID id) {
        VendaEntity entity = find("vendaId", id.toString()).firstResult();
        if(entity != null)
            delete(entity);
    }
}
