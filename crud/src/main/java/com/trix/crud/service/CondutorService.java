package com.trix.crud.service;

import com.trix.crud.dto.NovoCondutor;
import com.trix.crud.modelo.Condutor;
import com.trix.crud.repository.CondutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CondutorService {

    @Autowired
    CondutorRepository repository;

    public boolean cadastraNovoCondutor(NovoCondutor novoCondutor){
        if(verificaCnhCondutor(novoCondutor.getNumCnh()) && verificaNomeCondutor(novoCondutor.getNome())){
            repository.save(geraCondutor(novoCondutor));
            return true;
        }else{
            return false;
        }
    }

    public List<Condutor> consultaTodosCondutores(){
        try{
            return (List<Condutor>) repository.findAll();
        }catch (ClassCastException e ){e.printStackTrace();}
        return Collections.emptyList();
    }

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

    public ResponseEntity alteraCondutor(Condutor condutor){
       if(verificaNomeCondutor(condutor.getNomeCondutor())){
           repository.save(alteracaoCondutor(condutor));
           return ResponseEntity.ok("Alterações salvas com sucesso!");
       }
       return ResponseEntity.ok("Houve um problema ao salvar as alterações");
    }

    public ResponseEntity deletaCondutor(String cnh){
        if(verificaCnhCondutor(cnh) && repository.findById(cnh).isPresent()){
            repository.deleteById(cnh);
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok("Houve um problema na hora de remover o condutor");
        }

    }

    public void addVeiculoCondutor(Condutor condutor){
        repository.save(condutor);
    }

    public void rmvVeiculoCondutor(Condutor condutor){
        repository.save(condutor);
    }

    public ResponseEntity buscaNomeCondutor(String nomeCondutor){
        if(verificaNomeCondutor(nomeCondutor)){
            if(nomeCondutor.contains(" ")){
                return ResponseEntity.ok(repository.findByNomeCondutor(nomeCondutor));
            }
            return ResponseEntity.ok(repository.findByNomeCondutorContaining(nomeCondutor));
        }
        return ResponseEntity.ok("Nome inválido!");
    }

    private boolean verificaCnhCondutor(String cnh){
        return cnh.matches("(?=.*\\d).{11}") && !cnh.matches("(?=.*[a-zA-Z]).+");
    }
  
    private boolean verificaNomeCondutor(String nome){
        return !nome.isBlank() && nome.matches("(?=.*[a-zA-Z]).{2,}") && !nome.matches("(?=.*\\d).+");
    }

    private Condutor geraCondutor(NovoCondutor novoCondutor){
        Condutor condutor = new Condutor();
        condutor.setNomeCondutor(novoCondutor.getNome());
        condutor.setNumeroCnh(novoCondutor.getNumCnh());
        condutor.setListaDeVeiculos(new ArrayList<>());
        return condutor;

    }

    private Condutor alteracaoCondutor(Condutor condutorComAlteracao){
        Condutor existente = repository.findById(condutorComAlteracao.getNumeroCnh()).get();
        existente.setNomeCondutor(condutorComAlteracao.getNomeCondutor());
        return existente;
    }
}
