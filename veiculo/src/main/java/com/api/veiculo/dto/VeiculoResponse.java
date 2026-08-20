package com.api.veiculo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "DTO de resposta representando um veículo cadastrado")
public record VeiculoResponse(
        @Schema(description = "Identificador único (UUID)")
        UUID veiculoId,
        @Schema(description = "Renavam do veículo")
        String renavam,
        @Schema(description = "Placa do veículo")
        String placa,
        @Schema(description = "Chassi do veículo")
        String chassi,
        @Schema(description = "Ano do modelo")
        String anoModelo,
        @Schema(description = "Ano de fabricação")
        String anoFabricacao,
        @Schema(description = "Cor predominante")
        String cor,
        @Schema(description = "UF da placa")
        String ufPlaca,
        @Schema(description = "Data de aquisição")
        LocalDate dataAquisicao,
        @Schema(description = "CNH do condutor vinculado")
        String cnhCondutor
) {
}
