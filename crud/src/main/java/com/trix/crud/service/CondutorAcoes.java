package com.trix.crud.service;

import com.trix.crud.dto.NovoCondutor;
import com.trix.crud.modelo.Condutor;
import com.trix.crud.repository.CondutorRepository;
import com.trix.crud.service.interfaces.AcoesCondutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CondutorAcoes implements AcoesCondutor{

    private final CondutorRepository repository;

    public CondutorAcoes(CondutorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Condutor geraCondutor(NovoCondutor novoCondutor) {
        Condutor condutor = new Condutor();
        condutor.setNomeCondutor(novoCondutor.getNome());
        condutor.setNumeroCnh(novoCondutor.getNumCnh());
        condutor.setListaDeVeiculos(new ArrayList<>());
        return condutor;
    }

    @Override
    public Condutor alteracaoCondutor(Condutor condutorComAlteracao) {
        Condutor existente = repository.findById(condutorComAlteracao.getNumeroCnh()).get();
        existente.setNomeCondutor(condutorComAlteracao.getNomeCondutor());
        return existente;
    }
}
