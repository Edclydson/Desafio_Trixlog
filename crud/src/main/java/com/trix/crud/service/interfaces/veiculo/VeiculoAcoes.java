package com.trix.crud.service.interfaces.veiculo;

import com.trix.crud.dto.NovoVeiculo;
import com.trix.crud.modelo.Veiculo;

import java.util.Date;
import java.util.List;

public interface VeiculoAcoes{

    boolean atribuirCondutorAoVeiculo(String renavam, String cnh);

    Veiculo LiberacaoVeiculo(String renavam);

    Date dataDaAquisicao();

    Date converteStringtoData(String data);

    Veiculo criaVeiculo(NovoVeiculo novoVeiculo);

    Veiculo atualizacaoDados(NovoVeiculo veiculo, Veiculo existente);

    List<Veiculo> addVeiculoListaCondutor(List<Veiculo> lista, String renavam);
}
