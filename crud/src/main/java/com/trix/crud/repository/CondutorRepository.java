package com.trix.crud.repository;

import com.trix.crud.modelo.Condutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CondutorRepository extends CrudRepository<Condutor,String>{
    
    List<Condutor> findByNomeCondutorContaining(String nome_condutor);
    
    List<Condutor> findByNomeCondutor(String nome_condutor);

    @Override
    List<Condutor> findAll();
}
