package com.guilhermehns.application.usecase.relatorio;

import com.guilhermehns.application.dto.FaturamentoMensalItemDTO;
import com.guilhermehns.application.dto.RelatorioFaturamentoMensalDTO;
import com.guilhermehns.application.dto.ResumoFaturamentoMensalDTO;
import com.guilhermehns.domain.repository.VendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RelatorioFaturamentoMensalUseCaseTest {
    private VendaRepository vendaRepository;
    private RelatorioFaturamentoMensalUseCase useCase;

    @BeforeEach
    void setUp() {
        vendaRepository = Mockito.mock(VendaRepository.class);
        useCase = new RelatorioFaturamentoMensalUseCase(vendaRepository);
    }

    @Test
    void deveMontarRelatorioFaturamentoMensalComResumoEPreencherMesesZerados() {
        FaturamentoMensalItemDTO marco2026 = new FaturamentoMensalItemDTO();
        marco2026.setAno(2026);
        marco2026.setMes(3);
        marco2026.setFaturamento(new BigDecimal("100.00"));

        FaturamentoMensalItemDTO janeiro2026 = new FaturamentoMensalItemDTO();
        janeiro2026.setAno(2026);
        janeiro2026.setMes(1);
        janeiro2026.setFaturamento(new BigDecimal("200.00"));

        Mockito.when(vendaRepository.buscaFaturamentoMensal(LocalDate.of(2026, 3, 20)))
                .thenReturn(List.of(marco2026, janeiro2026));

        RelatorioFaturamentoMensalDTO resultado = useCase.executar("2026-03-20");

        assertEquals(12, resultado.getFaturamentosMensais().size());

        assertEquals(2026, resultado.getFaturamentosMensais().get(0).getAno());
        assertEquals(3, resultado.getFaturamentosMensais().get(0).getMes());
        assertEquals(new BigDecimal("100.00"), resultado.getFaturamentosMensais().get(0).getFaturamento());
        assertEquals(new BigDecimal("9.00"), resultado.getFaturamentosMensais().get(0).getImposto());

        assertEquals(2026, resultado.getFaturamentosMensais().get(1).getAno());
        assertEquals(2, resultado.getFaturamentosMensais().get(1).getMes());
        assertEquals(new BigDecimal("0.00"), resultado.getFaturamentosMensais().get(1).getFaturamento());
        assertEquals(new BigDecimal("0.00"), resultado.getFaturamentosMensais().get(1).getImposto());

        assertEquals(2026, resultado.getFaturamentosMensais().get(2).getAno());
        assertEquals(1, resultado.getFaturamentosMensais().get(2).getMes());
        assertEquals(new BigDecimal("200.00"), resultado.getFaturamentosMensais().get(2).getFaturamento());
        assertEquals(new BigDecimal("18.00"), resultado.getFaturamentosMensais().get(2).getImposto());

        ResumoFaturamentoMensalDTO resumo = resultado.getResumo();
        assertEquals(new BigDecimal("300.00"), resumo.getFaturamentoLoja());
        assertEquals(new BigDecimal("27.00"), resumo.getImpostoTotal());
        assertEquals(new BigDecimal("327.00"), resumo.getFaturamentoMaisImposto());

        Mockito.verify(vendaRepository, Mockito.times(1))
                .buscaFaturamentoMensal(LocalDate.of(2026, 3, 20));
        Mockito.verifyNoMoreInteractions(vendaRepository);
    }

    @Test
    void deveRetornarDozeMesesZeradosQuandoNaoHouverFaturamentoNoPeriodo() {
        Mockito.when(vendaRepository.buscaFaturamentoMensal(LocalDate.of(2026, 3, 20)))
                .thenReturn(List.of());

        RelatorioFaturamentoMensalDTO resultado = useCase.executar("2026-03-20");

        assertEquals(12, resultado.getFaturamentosMensais().size());

        resultado.getFaturamentosMensais().forEach(item -> {
            assertEquals(new BigDecimal("0.00"), item.getFaturamento());
            assertEquals(new BigDecimal("0.00"), item.getImposto());
        });

        ResumoFaturamentoMensalDTO resumo = resultado.getResumo();
        assertEquals(new BigDecimal("0.00"), resumo.getFaturamentoLoja());
        assertEquals(new BigDecimal("0.00"), resumo.getImpostoTotal());
        assertEquals(new BigDecimal("0.00"), resumo.getFaturamentoMaisImposto());

        Mockito.verify(vendaRepository, Mockito.times(1))
                .buscaFaturamentoMensal(LocalDate.of(2026, 3, 20));
        Mockito.verifyNoMoreInteractions(vendaRepository);
    }
}
