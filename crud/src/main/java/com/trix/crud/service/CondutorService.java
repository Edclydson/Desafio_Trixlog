package com.trix.crud.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.trix.crud.modelo.Veiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.trix.crud.dto.NovoCondutor;
import com.trix.crud.modelo.Condutor;
import com.trix.crud.repository.CondutorRepository;

@Service
public class CondutorService {

    @Autowired
    CondutorRepository repository;

    public boolean cadastraNovoCondutor(NovoCondutor novoCondutor){
        Condutor condutor = new Condutor();
        if(verificaCnhCondutor(novoCondutor.getNumCnh()) && verificaNomeCondutor(novoCondutor.getNome())){
            condutor.setNomeCondutor(novoCondutor.getNome());
            condutor.setNumeroCnh(novoCondutor.getNumCnh());
            condutor.setListaDeVeiculos(new ArrayList<>());
            repository.save(condutor);
            return true;
        }else{
            return false;
        }
    }

    public Iterable<Condutor> consultaTodosCondutores(){
        return repository.findAll();
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
       Condutor existente = repository.findById(condutor.getNumeroCnh()).get();
       if(verificaNomeCondutor(condutor.getNomeCondutor())){
           existente.setNomeCondutor(condutor.getNomeCondutor());
           repository.save(existente);
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

    public List<Condutor> buscaNomeCondutor(String nomeCondutor){
        if(nomeCondutor.contains(" ") == true){
            return repository.findByNomeCondutor(nomeCondutor);
        }
        else
        {
            return repository.findByNomeCondutorContaining(nomeCondutor);
        }
    }

    private boolean verificaCnhCondutor(String cnh){
        if(cnh.matches("(?=.*[0-9]).{11}")){
                return true;
        }
        return false;
    }
  
    private boolean verificaNomeCondutor(String nome){
        if(!nome.isBlank() && nome.matches("(?=.*[a-zA-Z]).{2,}")){
            return true;
        }
        return false;
    }
}
