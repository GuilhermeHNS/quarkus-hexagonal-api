package com.guilhermehns.application.usecase.relatorio;


import com.guilhermehns.application.dto.NovoClienteDTO;
import com.guilhermehns.domain.repository.ClienteRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class RelatorioNovosClientesUseCase {

    private final ClienteRepository clienteRepository;

    public RelatorioNovosClientesUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<NovoClienteDTO> executar(int ano) {
        validarAno(ano);
        return clienteRepository.buscarNovosClientesPorAno(ano);
    }

    private void validarAno(int ano) {
        int anoAtual = LocalDate.now().getYear();

        if( ano <= 0 || ano > anoAtual){
            throw new IllegalArgumentException("Ano informado é inválido.");
        }
    }
}