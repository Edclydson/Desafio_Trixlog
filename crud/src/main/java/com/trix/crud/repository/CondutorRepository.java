package com.trix.crud.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trix.crud.modelo.Condutor;

@Repository
public interface CondutorRepository extends CrudRepository<Condutor,String>{
    
    List<Condutor> findByNomeCondutorContaining(String nome_condutor);
    
    List<Condutor> findByNomeCondutor(String nome_condutor);
}
