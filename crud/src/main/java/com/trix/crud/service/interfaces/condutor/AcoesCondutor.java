package com.trix.crud.service.interfaces.condutor;

import com.trix.crud.dto.NewDriver;
import com.trix.crud.modelo.Condutor;

public interface AcoesCondutor{

    Condutor geraCondutor(NewDriver newDriver);

    Condutor alteracaoCondutor(Condutor condutorComAlteracao);

    Condutor acquireProcess(String cnh, String renavam);

    Condutor loseProcess(String cnh, String renavam);
}
