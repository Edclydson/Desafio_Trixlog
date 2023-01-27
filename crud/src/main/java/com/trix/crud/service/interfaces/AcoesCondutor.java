package com.trix.crud.service.interfaces;

import com.trix.crud.dto.NovoCondutor;
import com.trix.crud.modelo.Condutor;

public interface AcoesCondutor{

    Condutor geraCondutor(NovoCondutor novoCondutor);

    Condutor alteracaoCondutor(Condutor condutorComAlteracao);
}
