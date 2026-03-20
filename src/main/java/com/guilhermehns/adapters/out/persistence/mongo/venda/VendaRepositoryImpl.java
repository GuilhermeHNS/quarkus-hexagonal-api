package com.guilhermehns.adapters.out.persistence.mongo.venda;

import com.guilhermehns.application.dto.FaturamentoMensalItemDTO;
import com.guilhermehns.application.dto.ItemMaiorFaturamentoDTO;
import com.guilhermehns.application.dto.RelatorioFaturamentoMensalDTO;
import com.guilhermehns.application.dto.ResumoFaturamentoMensalDTO;
import com.guilhermehns.domain.model.cliente.Cliente;
import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.model.venda.ItemVenda;
import com.guilhermehns.domain.model.venda.Venda;
import com.guilhermehns.domain.repository.ClienteRepository;
import com.guilhermehns.domain.repository.ProdutoRepository;
import com.guilhermehns.domain.repository.VendaRepository;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class VendaRepositoryImpl implements VendaRepository, PanacheMongoRepository<VendaEntity> {

    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    private final Integer QTDE_MESES_RETROCEDENTES_FATURAMENTO_MENSAL = 12;

    private final Integer QTDE_ITENS_MAIOR_FATURAMENTO = 4;

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


            return venda;
        }).toList();
    }

    @Override
    public Optional<Venda> findById(UUID id) {
        VendaEntity entity = find("vendaId", id.toString()).firstResult();
        if (entity == null) {
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

        return Optional.of(venda);
    }

    @Override
    public void deleteById(UUID id) {
        VendaEntity entity = find("vendaId", id.toString()).firstResult();
        if (entity != null)
            delete(entity);
    }

    @Override
    public RelatorioFaturamentoMensalDTO buscaFaturamentoMensal(LocalDate dataReferencia) {
        LocalDate inicioPeriodo = dataReferencia.minusMonths(QTDE_MESES_RETROCEDENTES_FATURAMENTO_MENSAL -1).withDayOfMonth(1);
        LocalDate fimPeriodo = dataReferencia.withDayOfMonth(dataReferencia.lengthOfMonth());

        LocalDateTime inicioDataHora = inicioPeriodo.atStartOfDay();
        LocalDateTime fimDataHora = fimPeriodo.atTime(23, 59, 59);

        Document match = new Document("$match",
                new Document("dataVenda",
                        new Document("$gte", inicioDataHora)
                                .append("$lte", fimDataHora)));

        Document unwind = new Document("$unwind", "$itens");

        Document group = new Document("$group",
                new Document("_id",
                        new Document("ano", new Document("$year", "$dataVenda"))
                                .append("mes", new Document("$month", "$dataVenda"))
                ).append("faturamento",
                        new Document("$sum",
                                new Document("$multiply", List.of("$itens.quantidade", "$itens.valorUnitario"))
                        )
                )
        );

        Document sort = new Document("$sort",
                new Document("_id.ano", 1)
                        .append("_id.mes", 1)
        );

        List<Document> resultados = mongoCollection()
                .aggregate(List.of(match, unwind, group, sort), Document.class)
                .into(new ArrayList<>());

        List<FaturamentoMensalItemDTO> faturamentosEncontrados = resultados.stream().map(doc -> {
            Document id = (Document) doc.get("_id");

            FaturamentoMensalItemDTO dto = new FaturamentoMensalItemDTO();
            dto.setAno(id.getInteger("ano"));
            dto.setMes(id.getInteger("mes"));

            BigDecimal faturamento = new BigDecimal(doc.get("faturamento").toString()).setScale(2, RoundingMode.HALF_UP);
            dto.setFaturamento(faturamento);

            BigDecimal imposto = faturamento.multiply(new BigDecimal("0.09")).setScale(2, RoundingMode.HALF_UP);
            dto.setImposto(imposto);

            return dto;
        }).toList();

        Map<String, FaturamentoMensalItemDTO> faturamentoPorMes = faturamentosEncontrados.stream()
                .collect(Collectors.toMap(
                        dto -> dto.getAno() + "-" + dto.getMes(),
                        dto -> dto
                ));

        List<FaturamentoMensalItemDTO> faturamentoCompleto = new ArrayList<>();

        for (int i = 0; i < QTDE_MESES_RETROCEDENTES_FATURAMENTO_MENSAL; i++) {
            LocalDate mesAtual = dataReferencia.withDayOfMonth(1).minusMonths(i);
            String chave = mesAtual.getYear() + "-" + mesAtual.getMonthValue();

            FaturamentoMensalItemDTO dtoExistente = faturamentoPorMes.get(chave);
            if (dtoExistente == null) {
                dtoExistente = new FaturamentoMensalItemDTO();
                dtoExistente.setAno(mesAtual.getYear());
                dtoExistente.setMes(mesAtual.getMonthValue());
                dtoExistente.setFaturamento(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                dtoExistente.setImposto(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            }

            faturamentoCompleto.add(dtoExistente);
        }

        BigDecimal faturamentoLoja = faturamentoCompleto.stream()
                .map(FaturamentoMensalItemDTO::getFaturamento)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal impostoTotal = faturamentoCompleto.stream()
                .map(FaturamentoMensalItemDTO::getImposto)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal faturamentoMaisImposto = faturamentoLoja
                .add(impostoTotal)
                .setScale(2, RoundingMode.HALF_UP);

        ResumoFaturamentoMensalDTO resumo = new ResumoFaturamentoMensalDTO();
        resumo.setFaturamentoLoja(faturamentoLoja);
        resumo.setImpostoTotal(impostoTotal);
        resumo.setFaturamentoMaisImposto(faturamentoMaisImposto);

        RelatorioFaturamentoMensalDTO relatorio = new RelatorioFaturamentoMensalDTO();
        relatorio.setFaturamentosMensais(faturamentoCompleto);
        relatorio.setResumo(resumo);

        return relatorio;
    }

    @Override
    public List<ItemMaiorFaturamentoDTO> buscarProdutosComMaiorFaturamento() {
        Document unwind = new Document("$unwind", "$itens");

        Document group = new Document("$group",
                new Document("_id", "$itens.produtoId")
                        .append("faturamento",
                                new Document("$sum",
                                        new Document("$multiply", List.of("$itens.quantidade", "$itens.valorUnitario"))
                                )
                        )
        );

        Document sort = new Document("$sort", new Document("faturamento", -1));
        Document limit = new Document("$limit", QTDE_ITENS_MAIOR_FATURAMENTO);

        List<Document> resultados = mongoCollection()
                .aggregate(List.of(unwind, group, sort, limit), Document.class)
                .into(new ArrayList<>());

        return resultados.stream().map(doc -> {
            UUID produtoId = UUID.fromString(doc.getString("_id"));

            Produto produto = produtoRepository.findById(produtoId).orElse(null);

            ItemMaiorFaturamentoDTO dto = new ItemMaiorFaturamentoDTO();
            dto.setProdutoId(produtoId);

            if (produto != null) {
                dto.setNomeProduto(produto.getNome());
                dto.setPrecoVenda(produto.getPrecoVenda());
            }

            return dto;
        }).toList();
    }
}
