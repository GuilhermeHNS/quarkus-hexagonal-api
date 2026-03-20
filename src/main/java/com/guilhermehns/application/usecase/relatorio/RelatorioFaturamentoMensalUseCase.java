package com.guilhermehns.application.usecase.relatorio;

import com.guilhermehns.application.dto.FaturamentoMensalItemDTO;
import com.guilhermehns.application.dto.RelatorioFaturamentoMensalDTO;
import com.guilhermehns.application.dto.ResumoFaturamentoMensalDTO;
import com.guilhermehns.domain.model.venda.ItemVenda;
import com.guilhermehns.domain.repository.VendaRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class RelatorioFaturamentoMensalUseCase {

    private final VendaRepository vendaRepository;
    private static final int QTDE_MESES_RETROCEDENTES_FATURAMENTO_MENSAL = 12;
    private static final BigDecimal ALIQUOTA_IMPOSTO = new BigDecimal("0.09");


    public RelatorioFaturamentoMensalUseCase(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    public RelatorioFaturamentoMensalDTO executar(String dataReferencia) {
        LocalDate data = LocalDate.parse(dataReferencia);
        List<FaturamentoMensalItemDTO> faturamentosEncontrados = vendaRepository.buscaFaturamentoMensal(data);

        Map<String, FaturamentoMensalItemDTO> faturamentoPorMes = faturamentosEncontrados.stream()
                .collect(Collectors.toMap(
                        dto -> dto.getAno() + "-" + dto.getMes(),
                        dto -> dto
                ));

        List<FaturamentoMensalItemDTO> faturamentoCompleto = new ArrayList<>();

        for (int i = 0; i < QTDE_MESES_RETROCEDENTES_FATURAMENTO_MENSAL; i++) {
            LocalDate mesAtual = data.withDayOfMonth(1).minusMonths(i);
            String chave = mesAtual.getYear() + "-" + mesAtual.getMonthValue();

            FaturamentoMensalItemDTO dtoExistente = faturamentoPorMes.get(chave);

            if (dtoExistente == null) {
                dtoExistente = new FaturamentoMensalItemDTO();
                dtoExistente.setAno(mesAtual.getYear());
                dtoExistente.setMes(mesAtual.getMonthValue());
                dtoExistente.setFaturamento(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            }

            BigDecimal imposto = dtoExistente.getFaturamento()
                    .multiply(ALIQUOTA_IMPOSTO)
                    .setScale(2, RoundingMode.HALF_UP);

            dtoExistente.setImposto(imposto);

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
}