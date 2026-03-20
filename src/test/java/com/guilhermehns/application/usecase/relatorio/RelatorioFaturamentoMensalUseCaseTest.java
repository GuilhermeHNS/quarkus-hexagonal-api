package com.guilhermehns.application.usecase.relatorio;

import com.guilhermehns.application.dto.FaturamentoMensalDTO;
import com.guilhermehns.domain.model.produto.Produto;
import com.guilhermehns.domain.model.venda.ItemVenda;
import com.guilhermehns.domain.model.venda.Venda;
import com.guilhermehns.domain.repository.VendaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RelatorioFaturamentoMensalUseCaseTest {
    @Test
    void deveRetornarFaturamentoMensalComBaseNaDataReferenciaInformada() {
        VendaRepository vendaRepository = Mockito.mock(VendaRepository.class);
        RelatorioFaturamentoMensalUseCase useCase = new RelatorioFaturamentoMensalUseCase(vendaRepository);

        LocalDate dataBase = LocalDate.parse("2026-03-20");

        FaturamentoMensalDTO dto = new FaturamentoMensalDTO();
        dto.setAno(2026);
        dto.setMes(3);
        dto.setFaturamentoBruto(new BigDecimal("42719.20"));
        dto.setImposto(new BigDecimal("3844.73"));
        dto.setFaturamentoLiquido(new BigDecimal("38874.47"));

        List<FaturamentoMensalDTO> retornoEsperado = List.of(dto);

        Mockito.when(vendaRepository.buscaFaturamentoMensal(dataBase))
                .thenReturn(retornoEsperado);

        List<FaturamentoMensalDTO> resultado = useCase.executar("2026-03-20");

        assertEquals(1, resultado.size());
        assertEquals(2026, resultado.get(0).getAno());
        assertEquals(3, resultado.get(0).getMes());
        assertEquals(new BigDecimal("42719.20"), resultado.get(0).getFaturamentoBruto());
        assertEquals(new BigDecimal("3844.73"), resultado.get(0).getImposto());
        assertEquals(new BigDecimal("38874.47"), resultado.get(0).getFaturamentoLiquido());

        Mockito.verify(vendaRepository).buscaFaturamentoMensal(dataBase);
    }
}
