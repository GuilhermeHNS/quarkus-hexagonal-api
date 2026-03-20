package com.guilhermehns.adapters.out.persistence.mongo.produto;

import com.guilhermehns.application.dto.EncalhadoDTO;
import com.guilhermehns.domain.model.produto.Dimensoes;
import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.repository.ProdutoRepository;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.Document;
import org.bson.types.Decimal128;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@ApplicationScoped
public class ProdutoRepositoryImpl implements ProdutoRepository, PanacheMongoRepository<ProdutoEntity> {

    private final Integer LIMIT_PRODUTOS_ENCALHADOS = 3;

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
        List<ProdutoEntity> entities = findAll(Sort.by("nome")).list();

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

    @Override
    public List<EncalhadoDTO> buscarProdutosEncalhados() {

        Document project = new Document("$project",
                new Document("_id", 0)
                        .append("produtoId", "$produtoId")
                        .append("nome", "$nome")
                        .append("peso", "$peso")
                        .append("precoCompra", "$precoCompra")
                        .append("dataCadastro", "$dataCadastro")
        );

        Document sortMaisAntigos = new Document("$sort",
                new Document("dataCadastro", 1));

        Document limit = new Document("$limit", LIMIT_PRODUTOS_ENCALHADOS);

        Document sortMaisCaros = new Document("$sort",
                new Document("precoCompra", -1));

        List<Document> pipeline = List.of(
                sortMaisAntigos,
                limit,
                sortMaisCaros,
                project
        );

        List<Document> documentos = mongoCollection()
                .aggregate(pipeline, Document.class)
                .into(new ArrayList<>());


        return documentos.stream()
                .map(doc -> {
                    EncalhadoDTO dto = new EncalhadoDTO();
                    dto.setProdutoId(UUID.fromString(doc.getString("produtoId")));
                    dto.setNomeProduto(doc.getString("nome"));
                    Object pesoObj = doc.get("peso");
                    if (pesoObj instanceof Decimal128 decimal128) {
                        dto.setPeso(decimal128.bigDecimalValue());
                    } else if (pesoObj instanceof Number number) {
                        dto.setPeso(BigDecimal.valueOf(number.doubleValue()));
                    }

                    Object precoCompraObj = doc.get("precoCompra");
                    if (precoCompraObj instanceof Decimal128 decimal128) {
                        dto.setPrecoCompra(decimal128.bigDecimalValue());
                    } else if (precoCompraObj instanceof Number number) {
                        dto.setPrecoCompra(BigDecimal.valueOf(number.doubleValue()));
                    }

                    Object dataCadastroObj = doc.get("dataCadastro");
                    if (dataCadastroObj instanceof Date date) {
                        dto.setDataCadastro(date.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime());
                    }
                    return dto;
                }).toList();
    }
}
