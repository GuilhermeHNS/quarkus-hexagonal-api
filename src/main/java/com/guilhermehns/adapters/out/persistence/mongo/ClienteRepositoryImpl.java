package com.guilhermehns.adapters.out.persistence.mongo;

import com.guilhermehns.domain.model.Cliente;
import com.guilhermehns.domain.repository.ClienteRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

import static io.quarkus.mongodb.panache.PanacheMongoEntityBase.persist;

@ApplicationScoped
public class ClienteRepositoryImpl implements ClienteRepository {
    @Override
    public Cliente save(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity();
        entity.clienteId = cliente.getId().toString();
        entity.nomeCompleto = cliente.getNomeCompleto();
        entity.nomeMae = cliente.getNomeMae();
        entity.cpf = cliente.getCpf();
        entity.rg = cliente.getRg();
        entity.email = cliente.getEmail();
        entity.telefone = cliente.getTelefone();
        entity.dataNascimento = cliente.getDataNascimento();
        entity.dataCadastro = cliente.getDataCadastro();

        if (cliente.getEndereco() != null) {
            EnderecoEntity enderecoEntity = new EnderecoEntity();
            enderecoEntity.cep = cliente.getEndereco().getCep();
            enderecoEntity.logradouro = cliente.getEndereco().getLogradouro();
            enderecoEntity.numero = cliente.getEndereco().getNumero();
            enderecoEntity.complemento = cliente.getEndereco().getComplemento();
            enderecoEntity.bairro = cliente.getEndereco().getBairro();
            enderecoEntity.cidade = cliente.getEndereco().getCidade();
            enderecoEntity.estado = cliente.getEndereco().getEstado();

            entity.endereco = enderecoEntity;
        }

        persist(entity);

        return cliente;
    }

    @Override
    public Optional<Cliente> getById(String id) {
        return Optional.empty();
    }

    @Override
    public List<Cliente> list() {
        return List.of();
    }

    @Override
    public void deleteById(String id) {

    }
}
