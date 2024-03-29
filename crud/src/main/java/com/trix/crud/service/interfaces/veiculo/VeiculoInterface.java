package com.trix.crud.service.interfaces.veiculo;

import com.trix.crud.dto.NovoVeiculo;
import com.trix.crud.modelo.Veiculo;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;

public interface VeiculoInterface {

    ResponseEntity cadastrarNovoVeiculo(NovoVeiculo novoVeiculo, URI uri);

    List<Veiculo> findAll();

    ResponseEntity buscaVeiculoComRenavam(String renavam);

    ResponseEntity buscaVeiculoComUfDaPlaca(String ufDaPlaca);

    ResponseEntity buscaVeiculoComPlaca(String placa);

    List<Veiculo> buscaPorParteDaPlaca(String placa);

    ResponseEntity buscaVeiculosComIntervaloAquisicao(String dataInicial, String dataFinal);

    ResponseEntity alterarDadosVeiculo(NovoVeiculo veiculo);

    ResponseEntity deletarVeiculo(String renavam);

}
