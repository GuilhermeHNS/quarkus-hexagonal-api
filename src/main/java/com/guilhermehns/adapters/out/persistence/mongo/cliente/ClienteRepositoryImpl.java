package com.guilhermehns.adapters.out.persistence.mongo.cliente;

import com.guilhermehns.application.dto.NovoClienteDTO;
import com.guilhermehns.domain.model.cliente.Cliente;
import com.guilhermehns.domain.model.cliente.Endereco;
import com.guilhermehns.domain.repository.ClienteRepository;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

@ApplicationScoped
public class ClienteRepositoryImpl implements ClienteRepository, PanacheMongoRepository<ClienteEntity> {
    @Override
    public Cliente save(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity();

        ClienteEntity existingEntity = find("clienteId", cliente.getId().toString()).firstResult();
        if (existingEntity != null) {
            entity.id = existingEntity.id;
        }

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

        persistOrUpdate(entity);

        return cliente;
    }

    @Override
    public Optional<Cliente> findById(UUID id) {
        ClienteEntity entity = find("clienteId", id.toString()).firstResult();

        if(entity == null) {
            return Optional.empty();
        }

        Cliente cliente = new Cliente();
        cliente.setId(UUID.fromString(entity.clienteId));
        cliente.setNomeCompleto(entity.nomeCompleto);
        cliente.setNomeMae(entity.nomeMae);
        cliente.setCpf(entity.cpf);
        cliente.setRg(entity.rg);
        cliente.setEmail(entity.email);
        cliente.setTelefone(entity.telefone);
        cliente.setDataNascimento(entity.dataNascimento);
        cliente.setDataCadastro(entity.dataCadastro);

        if (entity.endereco != null) {
            Endereco endereco = new Endereco();
            endereco.setCep(entity.endereco.cep);
            endereco.setLogradouro(entity.endereco.logradouro);
            endereco.setNumero(entity.endereco.numero);
            endereco.setComplemento(entity.endereco.complemento);
            endereco.setBairro(entity.endereco.bairro);
            endereco.setCidade(entity.endereco.cidade);
            endereco.setEstado(entity.endereco.estado);

            cliente.setEndereco(endereco);
        }

        return Optional.of(cliente);
    }

    @Override
    public List<Cliente> findAllClientes() {
        return listAll().stream().map(entity -> {
            Cliente cliente = new Cliente();

            cliente.setId(UUID.fromString(entity.clienteId));
            cliente.setNomeCompleto(entity.nomeCompleto);
            cliente.setNomeMae(entity.nomeMae);
            cliente.setCpf(entity.cpf);
            cliente.setRg(entity.rg);
            cliente.setEmail(entity.email);
            cliente.setTelefone(entity.telefone);
            cliente.setDataNascimento(entity.dataNascimento);
            cliente.setDataCadastro(entity.dataCadastro);

            if (entity.endereco != null) {
                Endereco endereco = new Endereco();
                endereco.setCep(entity.endereco.cep);
                endereco.setLogradouro(entity.endereco.logradouro);
                endereco.setNumero(entity.endereco.numero);
                endereco.setComplemento(entity.endereco.complemento);
                endereco.setBairro(entity.endereco.bairro);
                endereco.setCidade(entity.endereco.cidade);
                endereco.setEstado(entity.endereco.estado);

                cliente.setEndereco(endereco);
            }

            return cliente;
        }).toList();
    }

    @Override
    public void deleteById(UUID id) {
        ClienteEntity entity = find("clienteId", id.toString()).firstResult();

        if(entity != null) {
            delete(entity);
        }
    }

    @Override
    public List<NovoClienteDTO> buscarNovosClientesPorAno(int ano) {
        LocalDateTime inicioAno = LocalDateTime.of(ano, 1, 1, 0, 0, 0);
        LocalDateTime fimAno = LocalDateTime.of(ano, 12, 31, 23, 59, 59);

        Document match = new Document("$match",
                new Document("dataCadastro",
                        new Document("$gte", inicioAno)
                                .append("$lte", fimAno)));

        Document project = new Document("$project",
                new Document("_id", 0)
                        .append("clienteId", "$clienteId")
                        .append("nomeCompleto", "$nomeCompleto")
                        .append("dataNascimento", "$dataNascimento")
                        .append("dataCadastro", "$dataCadastro")
        );

        List<Document> result = mongoCollection()
                .aggregate(List.of(match, project), Document.class)
                .into(new ArrayList<>());

        return result.stream()
                .map(doc -> {
                    NovoClienteDTO dto = new NovoClienteDTO();
                    dto.setClienteId(UUID.fromString(doc.getString("clienteId")));
                    dto.setNomeCompleto(doc.getString("nomeCompleto"));
                    Date dataNascimento = doc.getDate("dataNascimento");
                    if (dataNascimento != null) {
                        dto.setDataNascimento(dataNascimento.toInstant()
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate());
                    }

                    Date dataCadastro = doc.getDate("dataCadastro");
                    if (dataCadastro != null) {
                        dto.setDataCadastro(dataCadastro.toInstant()
                                .atZone(ZoneOffset.UTC)
                                .toLocalDateTime());
                    }
                    return dto;
                }).toList();
    }
}
