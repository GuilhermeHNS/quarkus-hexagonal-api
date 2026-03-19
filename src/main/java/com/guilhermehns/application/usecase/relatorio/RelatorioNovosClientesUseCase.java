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
        return clienteRepository.findAllClientes().stream()
                .filter(cliente -> cliente.getDataCadastro() != null
                        && cliente.getDataCadastro().getYear() == ano)
                .map(cliente -> new NovoClienteDTO(
                        cliente.getId(),
                        cliente.getNomeCompleto(),
                        cliente.getDataNascimento(),
                        cliente.getDataCadastro()
                ))
                .toList();
    }
}