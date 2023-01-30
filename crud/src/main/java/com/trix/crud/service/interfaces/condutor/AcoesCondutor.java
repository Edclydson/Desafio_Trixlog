package com.trix.crud.service.interfaces.condutor;

import com.trix.crud.dto.NovoCondutor;
import com.trix.crud.modelo.Condutor;

public interface AcoesCondutor{

    Condutor geraCondutor(NovoCondutor novoCondutor);

    Condutor alteracaoCondutor(Condutor condutorComAlteracao);

    Condutor acquireProcess(String cnh, String renavam);

    Condutor loseProcess(String cnh, String renavam);
}
