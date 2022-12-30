package com.trix.crud.service;

import com.trix.crud.dto.NovoCondutor;
import com.trix.crud.modelo.Condutor;
import com.trix.crud.modelo.Veiculo;
import com.trix.crud.repository.CondutorRepository;
import com.trix.crud.service.interfaces.CondutorInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CondutorService implements CondutorInterface{

    @Autowired
    CondutorRepository repository;

    @Autowired
    VeiculoService veiculoService;

    @Override
    public boolean cadastraNovoCondutor(NovoCondutor novoCondutor){
        if(verificaCnhCondutor(novoCondutor.getNumCnh()) && verificaNomeCondutor(novoCondutor.getNome())){
            repository.save(geraCondutor(novoCondutor));
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List<Condutor> consultaTodosCondutores(){
        try{
            return (List<Condutor>) repository.findAll();
        }catch (ClassCastException e ){e.printStackTrace();}
        return Collections.emptyList();
    }

    @Override
    public ResponseEntity consultaCondutorcnh(String cnh){
        if(verificaCnhCondutor(cnh)){
            if(repository.findById(cnh).isPresent())
                return ResponseEntity.ok(repository.findById(cnh).get());
            else{
                return ResponseEntity.ok("Condutor não encontrado!");
            }
        }
            return ResponseEntity.ok("CNH informada não é válida!");
    }

    @Override
    public ResponseEntity alteraCondutor(Condutor condutor){
       if(verificaNomeCondutor(condutor.getNomeCondutor())){
           repository.save(alteracaoCondutor(condutor));
           return ResponseEntity.ok("Alterações salvas com sucesso!");
       }
       return ResponseEntity.ok("Houve um problema ao salvar as alterações");
    }

    @Override
    public ResponseEntity deletaCondutor(String cnh){
        if(verificaCnhCondutor(cnh) && repository.findById(cnh).isPresent()){
            repository.deleteById(cnh);
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok("Houve um problema na hora de remover o condutor");
        }

    }

    @Override
    public ResponseEntity buscaNomeCondutor(String nomeCondutor){
        if(verificaNomeCondutor(nomeCondutor)){
            if(nomeCondutor.contains(" ")){
                return ResponseEntity.ok(repository.findByNomeCondutor(nomeCondutor));
            }
            return ResponseEntity.ok(repository.findByNomeCondutorContaining(nomeCondutor));
        }
        return ResponseEntity.ok("Nome inválido!");
    }

    @Override
    public boolean verificaCnhCondutor(String cnh){
        return cnh.matches("(?=.*\\d).{11}") && !cnh.matches("(?=.*[a-zA-Z]).+");
    }

    @Override
    public boolean verificaNomeCondutor(String nome){
        return !nome.isBlank() && nome.matches("(?=.*[a-zA-Z]).{2,}") && !nome.matches("(?=.*\\d).+");
    }

    @Override
    public Condutor geraCondutor(NovoCondutor novoCondutor){
        Condutor condutor = new Condutor();
        condutor.setNomeCondutor(novoCondutor.getNome());
        condutor.setNumeroCnh(novoCondutor.getNumCnh());
        condutor.setListaDeVeiculos(new ArrayList<>());
        return condutor;

    }

    @Override
    public Condutor alteracaoCondutor(Condutor condutorComAlteracao){
        Condutor existente = repository.findById(condutorComAlteracao.getNumeroCnh()).get();
        existente.setNomeCondutor(condutorComAlteracao.getNomeCondutor());
        return existente;
    }

    @Override
    public ResponseEntity adquirirVeiculo(String renavam, String cnh){
        if(veiculoService.verificacaoVeiculoParaAquisicao(renavam) && verificacaoParaAquisicaoVeiculo(cnh)){
            Condutor condutorComVeiculo = repository.findById(cnh).get();
            condutorComVeiculo.getListaDeVeiculos().add(veiculoService.repository.findById(renavam).get());
            repository.save(condutorComVeiculo);
            veiculoService.atribuirCondutorAoVeiculo(renavam, cnh);
            return ResponseEntity.ok("Veículo adquirido com sucesso");
        }
        return ResponseEntity.ok("Para adquerir um veiculo informe os dados corretamente.");
    }

    @Override
    public ResponseEntity liberarVeiculo(String renavam, String cnh){
        if(verificacaoSeCondutorTemVeiculo(cnh) && verificaSeCondutorPossuioVeiculo(renavam, cnh)
        && verificaCnhCondutor(cnh)){
            Condutor condutor = repository.findById(cnh).get();
            condutor.getListaDeVeiculos().remove(veiculoService.LiberacaoVeiculo(renavam));
            repository.save(condutor);
            return ResponseEntity.ok("O Condutor não tem mais posse do veículo: " +renavam);
        }
        return ResponseEntity.ok("Requisição não foi processada! Tente novamente.");
    }
    @Override
    public boolean verificacaoParaAquisicaoVeiculo(String cnh){
        return verificaCnhCondutor(cnh) && repository.existsById(cnh);
    }

    @Override
    public boolean verificacaoSeCondutorTemVeiculo(String cnh){
            Condutor condutor = repository.findById(cnh).get();
            return (repository.existsById(cnh) && !condutor.getListaDeVeiculos().isEmpty());
    }

    @Override
    public boolean verificaSeCondutorPossuioVeiculo(String renavam, String cnh){
        List<Veiculo> veiculosCondutor = repository.findById(cnh).get().getListaDeVeiculos();
        for(Veiculo veiculo : veiculosCondutor){
            if(veiculo.getRenavam().equals(renavam)){
                return true;
            }
        }
        return false;
    }
}
