package com.trix.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trix.crud.modelo.Veiculo;

@Repository
public interface VeiculoRepository extends CrudRepository<Veiculo, String>{
    List<Veiculo> findByufPlaca(String uf);

    List<Veiculo> findByPlaca(String placa);

    List<Veiculo> findByPlacaContaining(String placa);

    @Query(value = "SELECT * FROM veiculo WHERE data_aquisicao BETWEEN :datainicio AND :datafim ", nativeQuery = true)
    List<Veiculo> findByintevalo(String datainicio,String datafim);

}
