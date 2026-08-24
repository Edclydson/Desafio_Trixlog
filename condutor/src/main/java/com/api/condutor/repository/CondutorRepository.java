package com.api.condutor.repository;

import com.api.condutor.modelo.Condutor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CondutorRepository extends CrudRepository<Condutor,String>{
    
    @EntityGraph(attributePaths = {"renavams"})
    List<Condutor> findByNomeCondutorContaining(String nome_condutor);
    
    @EntityGraph(attributePaths = {"renavams"})
    List<Condutor> findByNomeCondutor(String nome_condutor);

    @Override
    @EntityGraph(attributePaths = {"renavams"})
    List<Condutor> findAll();
}
