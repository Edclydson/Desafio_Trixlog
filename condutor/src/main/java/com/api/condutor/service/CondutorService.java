package com.api.condutor.service;

import com.api.condutor.dto.CondutorRequest;
import com.api.condutor.dto.CondutorResponse;
import com.api.condutor.mapper.CondutorMapper;
import com.api.condutor.modelo.Condutor;
import com.api.condutor.repository.CondutorRepository;
import com.api.condutor.service.interfaces.condutor.CondutorInterface;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CondutorService implements CondutorInterface {

    private final CondutorRepository repository;
    private final CondutorMapper mapper;

    public CondutorService(CondutorRepository repository, CondutorMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public CondutorResponse cadastraNovoCondutor(CondutorRequest request) {
        if (repository.existsById(request.numeroCnh())) {
            throw new IllegalArgumentException("CNH já cadastrada!");
        }
        try {
            Condutor condutor = mapper.toEntity(request);
            condutor.setRenavams(new ArrayList<>());
            Condutor salvo = repository.save(condutor);
            return mapper.toResponse(salvo);
        } catch (PersistenceException e) {
            throw new PersistenceException("Erro ao cadastrar novo condutor!", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CondutorResponse> consultaTodosCondutores() {
        List<Condutor> todosCondutores = repository.findAll();
        if (todosCondutores.isEmpty()) return Collections.emptyList();
        
        return todosCondutores.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CondutorResponse consultaCondutorcnh(String cnh) {
        Condutor condutor = repository.findById(cnh)
                .orElseThrow(() -> new EntityNotFoundException("Condutor não encontrado!"));
        return mapper.toResponse(condutor);
    }

    @Override
    @Transactional
    public CondutorResponse alteraCondutor(CondutorRequest request) {
        Condutor existente = repository.findById(request.numeroCnh())
                .orElseThrow(() -> new EntityNotFoundException("Condutor não encontrado!"));
        
        existente.setNomeCondutor(request.nomeCondutor());
        // O Hibernate fará o update automático no commit da transação (Dirty Checking)
        return mapper.toResponse(existente);
    }

    @Override
    @Transactional
    public void deletaCondutor(String cnh) {
        if (!repository.existsById(cnh)) {
            throw new EntityNotFoundException("Condutor não encontrado para deleção.");
        }
        repository.deleteById(cnh);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CondutorResponse> buscaNomeCondutor(String nomeCondutor) {
        List<Condutor> condutores;
        if (nomeCondutor.contains(" ")) {
            condutores = repository.findByNomeCondutor(nomeCondutor);
        } else {
            condutores = repository.findByNomeCondutorContaining(nomeCondutor);
        }
        return condutores.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CondutorResponse adquirirVeiculo(String renavam, String cnh) {
        Condutor condutor = repository.findById(cnh)
                .orElseThrow(() -> new EntityNotFoundException("Condutor não encontrado!"));
        
        if (condutor.getRenavams() == null) {
            condutor.setRenavams(new ArrayList<>());
        }
        
        if (!condutor.getRenavams().contains(renavam)) {
            condutor.getRenavams().add(renavam);
        }
        
        // Dirty checking entra em ação aqui
        return mapper.toResponse(condutor);
    }

    @Override
    @Transactional
    public CondutorResponse liberarVeiculo(String renavam, String cnh) {
        Condutor condutor = repository.findById(cnh)
                .orElseThrow(() -> new EntityNotFoundException("Condutor não encontrado!"));
        
        if (condutor.getRenavams() != null && condutor.getRenavams().contains(renavam)) {
            condutor.getRenavams().remove(renavam);
        } else {
            throw new IllegalArgumentException("O condutor não possui o veículo informado.");
        }
        
        // Dirty checking atuará na deleção do renavam na tabela da coleção
        return mapper.toResponse(condutor);
    }
}
