package com.guilhermehns.application.usecase.relatorio;


import com.guilhermehns.application.dto.NovoClienteDTO;
import com.guilhermehns.domain.repository.ClienteRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class RelatorioNovosClientesUseCase {

    private final ClienteRepository clienteRepository;

    public RelatorioNovosClientesUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<NovoClienteDTO> executar(int ano) {
        return clienteRepository.buscarNovosClientesPorAno(ano);
    }
}