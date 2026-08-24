package com.api.condutor.service.interfaces.condutor;

import com.api.condutor.dto.CondutorRequest;
import com.api.condutor.dto.CondutorResponse;

import java.util.List;

public interface CondutorInterface {

    CondutorResponse cadastraNovoCondutor(CondutorRequest request);

    List<CondutorResponse> consultaTodosCondutores();

    CondutorResponse consultaCondutorcnh(String cnh);

    CondutorResponse alteraCondutor(CondutorRequest request);

    void deletaCondutor(String cnh);

    List<CondutorResponse> buscaNomeCondutor(String nomeCondutor);

    CondutorResponse adquirirVeiculo(String renavam, String cnh);

    CondutorResponse liberarVeiculo(String renavam, String cnh);
}
