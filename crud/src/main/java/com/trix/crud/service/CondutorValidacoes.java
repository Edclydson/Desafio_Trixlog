package com.trix.crud.service;

import com.trix.crud.modelo.Condutor;
import com.trix.crud.modelo.Veiculo;
import com.trix.crud.repository.CondutorRepository;
import com.trix.crud.service.interfaces.ValidacoesCondutorInterface;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CondutorValidacoes implements ValidacoesCondutorInterface{

    private final CondutorRepository repository;

    public CondutorValidacoes(CondutorRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean cnhValida(String cnh) {
        return cnh.matches("(?=.*\\d).{11}") && !cnh.matches("(?=.*[a-zA-Z} {,.^?~=+_/*|]).+");
    }

    @Override
    public boolean nomeCondutor(String nome) {
        return !nome.isBlank() && nome.matches("(?=.*[a-zA-Z]).{2,}") && !nome.matches("(?=.*\\d).+");
    }

    @Override
    public boolean requisitosAquisicaoVeiculo(String cnh) {
        return cnhValida(cnh) && repository.existsById(cnh);
    }

    @Override
    public boolean temAlgumVeiculo(String cnh) {
        Condutor condutor = repository.findById(cnh).get();
        return (repository.existsById(cnh) && !condutor.getListaDeVeiculos().isEmpty());
    }

    @Override
    public boolean possuiOVeiculo(String renavam, String cnh) {
        List<Veiculo> veiculosCondutor = new ArrayList<>(repository.findById(cnh).get().getListaDeVeiculos());
        for(Veiculo veiculo : veiculosCondutor){
            if(veiculo.getRenavam().equals(renavam)){
                return true;
            }
        }
        return false;
    }
}
