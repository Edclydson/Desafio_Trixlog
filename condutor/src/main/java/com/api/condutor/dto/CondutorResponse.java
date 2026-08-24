package com.api.condutor.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO de saída representando as informações consolidadas de um Condutor.")
public record CondutorResponse(
        @Schema(description = "Número da CNH do condutor", example = "12345678901")
        String numeroCnh,
        
        @Schema(description = "Nome completo do condutor", example = "Zezin Danonão")
        String nomeCondutor,
        
        @Schema(description = "Lista de RENAVAMs vinculados à posse deste condutor", example = "[\"77546831291\", \"83921764500\"]")
        List<String> renavams
) {}
