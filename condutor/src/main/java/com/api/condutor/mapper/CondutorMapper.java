package com.api.condutor.mapper;

import com.api.condutor.dto.CondutorRequest;
import com.api.condutor.dto.CondutorResponse;
import com.api.condutor.modelo.Condutor;
import org.springframework.stereotype.Component;

@Component
public class CondutorMapper {

    public CondutorResponse toResponse(Condutor condutor) {
        if (condutor == null) {
            return null;
        }
        return new CondutorResponse(
                condutor.getNumeroCnh(),
                condutor.getNomeCondutor(),
                condutor.getRenavams()
        );
    }

    public Condutor toEntity(CondutorRequest request) {
        if (request == null) {
            return null;
        }
        Condutor condutor = new Condutor();
        condutor.setNumeroCnh(request.numeroCnh());
        condutor.setNomeCondutor(request.nomeCondutor());
        return condutor;
    }
}
