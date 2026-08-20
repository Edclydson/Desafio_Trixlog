package com.api.veiculo.repository;

import com.api.veiculo.model.Veiculo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface VeiculoRepository extends CrudRepository<Veiculo, UUID> {
    List<Veiculo> findAll();
    List<Veiculo> findByUfPlaca(String ufPlaca);
    List<Veiculo> findByPlaca(String placa);
    List<Veiculo> findByPlacaContaining(String placa);
    @Query(value = "SELECT * FROM veiculo WHERE data_aquisicao BETWEEN :datainicio AND :datafim", nativeQuery = true)
    List<Veiculo> findByIntervalo(LocalDate datainicio, LocalDate datafim);
    Veiculo findByRenavam(String renavam);
    boolean existsByRenavam(String renavam);
    boolean existsByPlaca(String placa);
    void deleteByRenavam(String renavam);
}
