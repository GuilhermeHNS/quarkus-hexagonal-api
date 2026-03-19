package com.guilhermehns.adapters.out.persistence.mongo.produto;

import com.guilhermehns.domain.model.produto.Dimensoes;
import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.repository.ProdutoRepository;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ProdutoRepositoryImpl implements ProdutoRepository, PanacheMongoRepository<ProdutoEntity> {
    @Override
    public Produto save(Produto produto) {
        ProdutoEntity entity = new ProdutoEntity();

        ProdutoEntity existingEntity = find("produtoId", produto.getId().toString()).firstResult();
        if (existingEntity != null) {
            entity.id = existingEntity.id;
        }

        entity.produtoId = produto.getId().toString();
        entity.nome = produto.getNome();
        entity.tipo = produto.getTipo();
        entity.detalhes = produto.getDetalhes();
        entity.peso = produto.getPeso();
        entity.precoCompra = produto.getPrecoCompra();
        entity.precoVenda = produto.getPrecoVenda();
        entity.dataCadastro = produto.getDataCadastro();

        if (produto.getDimensoes() != null) {
            DimensoesEntity dimensoesEntity = new DimensoesEntity();
            dimensoesEntity.altura = produto.getDimensoes().getAltura();
            dimensoesEntity.largura = produto.getDimensoes().getLargura();
            dimensoesEntity.profundidade = produto.getDimensoes().getProfundidade();

            entity.dimensoes = dimensoesEntity;
        }

        persistOrUpdate(entity);

        return produto;
    }

    @Override
    public List<Produto> findAllProdutos() {
        List<ProdutoEntity> entities = listAll();

        return entities.stream().map(entity -> {
            Produto produto = new Produto();
            produto.setId(UUID.fromString(entity.produtoId));
            produto.setNome(entity.nome);
            produto.setTipo(entity.tipo);
            produto.setDetalhes(entity.detalhes);
            produto.setPeso(entity.peso);
            produto.setPrecoCompra(entity.precoCompra);
            produto.setPrecoVenda(entity.precoVenda);
            produto.setDataCadastro(entity.dataCadastro);

            if (entity.dimensoes != null) {
                Dimensoes dimensoes = new Dimensoes();
                dimensoes.setAltura(entity.dimensoes.altura);
                dimensoes.setLargura(entity.dimensoes.largura);
                dimensoes.setProfundidade(entity.dimensoes.profundidade);

                produto.setDimensoes(dimensoes);
            }

            return produto;
        }).toList();
    }

    @Override
    public Optional<Produto> findById(UUID id) {
        ProdutoEntity entity = find("produtoId", id.toString()).firstResult();

        if (entity == null) {
            return Optional.empty();
        }

        Produto produto = new Produto();
        produto.setId(UUID.fromString(entity.produtoId));
        produto.setNome(entity.nome);
        produto.setTipo(entity.tipo);
        produto.setDetalhes(entity.detalhes);
        produto.setPeso(entity.peso);
        produto.setPrecoCompra(entity.precoCompra);
        produto.setPrecoVenda(entity.precoVenda);
        produto.setDataCadastro(entity.dataCadastro);

        if (entity.dimensoes != null) {
            Dimensoes dimensoes = new Dimensoes();
            dimensoes.setAltura(entity.dimensoes.altura);
            dimensoes.setLargura(entity.dimensoes.largura);
            dimensoes.setProfundidade(entity.dimensoes.profundidade);

            produto.setDimensoes(dimensoes);
        }

        return Optional.of(produto);
    }

    @Override
    public void deleteById(UUID id) {
        ProdutoEntity entity = find("produtoId", id.toString()).firstResult();

        if (entity != null) {
            delete(entity);
        }
    }
}
