package com.trix.crud.service;

import com.trix.crud.dto.NovoCondutor;
import com.trix.crud.modelo.Condutor;
import com.trix.crud.modelo.Veiculo;
import com.trix.crud.repository.CondutorRepository;
import com.trix.crud.repository.VeiculoRepository;
import com.trix.crud.service.interfaces.condutor.AcoesCondutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CondutorAcoes implements AcoesCondutor{

    private final CondutorRepository repository;
    private final VeiculoRepository veiculoRepository;
    private final VeiculoService service;

    public CondutorAcoes(CondutorRepository repository, VeiculoRepository veiculoRepository, VeiculoService service) {
        this.repository = repository;
        this.veiculoRepository = veiculoRepository;
        this.service = service;
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

    @Override
    public Condutor acquireProcess(String cnh, String renavam) {
        Condutor condutorComVeiculo = repository.findById(cnh).get();
        List<Veiculo> novaLista = new ArrayList<>(condutorComVeiculo.getListaDeVeiculos());
        //REMOVER ESSE VEICULO REPOSITORY PARA UM METODO VEICULO SERVICE
        novaLista.add(veiculoRepository.findById(renavam).get());
        condutorComVeiculo.setListaDeVeiculos(novaLista);
        return condutorComVeiculo;
    }

    @Override
    public Condutor loseProcess(String cnh, String renavam) {
        Condutor condutor = repository.findById(cnh).get();
        List<Veiculo> novaLista = new ArrayList<>(condutor.getListaDeVeiculos());
        novaLista.remove(service.LiberacaoVeiculo(renavam));
        condutor.setListaDeVeiculos(novaLista);
        return condutor;
    }
}
