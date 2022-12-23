package com.trix.crud.service;

import com.trix.crud.dto.NovoVeiculo;
import com.trix.crud.modelo.Veiculo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface VeiculoInterface{
    ResponseEntity cadastrarNovoVeiculo(NovoVeiculo novoVeiculo);

    List<Veiculo> findAll();

    ResponseEntity buscaVeiculoComRenavam(String renavam);

    ResponseEntity buscaVeiculoComUfDaPlaca(String ufDaPlaca);

    ResponseEntity buscaVeiculoComPlaca(String placa);

    ResponseEntity buscaVeiculosComIntervaloAquisicao(String dataInicial,String dataFinal);

    ResponseEntity alterarDadosVeiculo(Veiculo veiculo);

    ResponseEntity deletarVeiculo(String renavam);

    boolean validaPlaca(String placaVeiculo);

    boolean verificaTamanhoDaPlaca(String placaVeiculo);

    boolean validaRenavam(String renavam);

    boolean validaUf(String uf);

    boolean ufExistente(String uf);
}
