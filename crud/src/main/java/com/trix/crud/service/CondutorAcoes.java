package com.trix.crud.service;

import com.trix.crud.dto.NewDriver;
import com.trix.crud.modelo.Condutor;
import com.trix.crud.modelo.Veiculo;
import com.trix.crud.repository.CondutorRepository;
import com.trix.crud.service.interfaces.condutor.AcoesCondutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CondutorAcoes implements AcoesCondutor{

    private final CondutorRepository repository;
    private final VeiculoAcoes veiculoAcao;

    public CondutorAcoes(CondutorRepository repository, VeiculoAcoes veiculoAcao) {
        this.repository = repository;
        this.veiculoAcao = veiculoAcao;
    }

    @Override
    public Condutor geraCondutor(NewDriver newDriver) {
        Condutor condutor = new Condutor();
        condutor.setNomeCondutor(newDriver.nameDriver());
        condutor.setNumeroCnh(newDriver.cnhNumber());
        condutor.setListaDeVeiculos(new ArrayList<>());
        return condutor;
    }

    @Override
    public Condutor alteracaoCondutor(Condutor condutorComAlteracao) {
        Condutor existente = repository.findById(condutorComAlteracao.getNumeroCnh()).get();
        existente.setNomeCondutor(condutorComAlteracao.getNomeCondutor());
        return existente;
    }

    @Override
    public Condutor acquireProcess(String cnh, String renavam) {
        Condutor condutorComVeiculo = repository.findById(cnh).get();
        List<Veiculo> novaLista = new ArrayList<>(condutorComVeiculo.getListaDeVeiculos());
        condutorComVeiculo.setListaDeVeiculos(veiculoAcao.addVeiculoListaCondutor(novaLista, renavam));
        return condutorComVeiculo;
    }

    @Override
    public Condutor loseProcess(String cnh, String renavam) {
        Condutor condutor = repository.findById(cnh).get();
        List<Veiculo> novaLista = new ArrayList<>(condutor.getListaDeVeiculos());
        novaLista.remove(veiculoAcao.LiberacaoVeiculo(renavam));
        condutor.setListaDeVeiculos(novaLista);
        return condutor;
    }
}
