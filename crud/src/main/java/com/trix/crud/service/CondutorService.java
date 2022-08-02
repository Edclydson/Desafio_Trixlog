package com.trix.crud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trix.crud.dto.NovoCondutor;
import com.trix.crud.modelo.Condutor;
import com.trix.crud.repository.CondutorRepository;

@Service
public class CondutorService {

    @Autowired
    CondutorRepository repository;

    public void cadastraNovoCondutor(NovoCondutor novoCondutor){
        Condutor condutor = new Condutor();
        condutor.setNomeCondutor(novoCondutor.getNome());
        condutor.setNumeroCnh(novoCondutor.getNumCnh());
        
        repository.save(condutor);
    }

    public List<Condutor> consultaTodosCondutores(){
        return (List<Condutor>) repository.findAll();
    }

    public Optional<Condutor> consultaCondutorcnh(String cnh){
        return repository.findById(cnh);
    }

    public void alteraCondutor(Condutor condutor){
       Condutor existente = consultaCondutorcnh(condutor.getNumeroCnh()).get();
       existente.setNomeCondutor(condutor.getNomeCondutor());
       repository.save(existente);
    }

    public void deletaCondutor(String cnh){
        repository.deleteById(cnh);
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
  
    
}
