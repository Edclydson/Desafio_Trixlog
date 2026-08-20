package com.api.veiculo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para cadastro e atualização de um veículo")
public record NovoVeiculo(
        @Schema(description = "Renavam do veículo (11 dígitos)", example = "12345678901")
        @Pattern(regexp = "(?=.*\\d).{11}", message = "Renavam inválido!")
        String renavamNovoVeiculo,
        @Schema(description = "Placa do veículo (padrão Mercosul ou antigo)", example = "ABC1234")
        @Pattern(regexp = "[A-Z]{3}\\d[A-Z]\\d{2}|[A-Z]{3}\\d{4}", message = "Placa inválida!")
        String placaNovoVeiculo,
        @Schema(description = "Chassi do veículo", example = "9BWZZZ37ZKT000000")
        String chassiNovoVeiculo,
        @Schema(description = "Ano do modelo", example = "2023")
        String anoModeloNovoVeiculo,
        @Schema(description = "Ano de fabricação", example = "2023")
        String anoFabricacaoNovoVeiculo,
        @Schema(description = "Cor predominante", example = "Prata")
        String corNovoVeiculo,
        @Schema(description = "UF da placa do veículo", example = "SP")
        String ufPlacaNovoVeiculo,
        @Schema(description = "Data de aquisição no formato dd-MM-yyyy", example = "20-08-2026")
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate dataAquisicaoNovoVeiculo,
        @Schema(description = "CNH do condutor vinculado", example = "12345678901")
        String chnCondutorNovoVeiculo

) {
}
