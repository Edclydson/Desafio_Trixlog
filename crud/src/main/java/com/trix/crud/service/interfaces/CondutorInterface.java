package com.trix.crud.service.interfaces;

import com.trix.crud.dto.NovoCondutor;
import com.trix.crud.modelo.Condutor;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CondutorInterface{

    boolean cadastraNovoCondutor(NovoCondutor novoCondutor);

    List<Condutor> consultaTodosCondutores();

    ResponseEntity consultaCondutorcnh(String cnh);

    ResponseEntity alteraCondutor(Condutor condutor);

    ResponseEntity deletaCondutor(String cnh);

    ResponseEntity buscaNomeCondutor(String nomeCondutor);

    Condutor geraCondutor(NovoCondutor novoCondutor);

    Condutor alteracaoCondutor(Condutor condutorComAlteracao);
}