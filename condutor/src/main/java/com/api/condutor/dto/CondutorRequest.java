package com.api.condutor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "DTO de entrada para criação ou atualização de um Condutor.")
public record CondutorRequest(
        @Schema(description = "Número da CNH do condutor. Deve conter 11 dígitos numéricos.", example = "12345678901")
        @NotBlank(message = "A CNH não pode ser vazia.")
        @Pattern(regexp = "(?=.*\\d).{11}", message = "A CNH deve conter 11 dígitos numéricos.")
        String numeroCnh,

        @Schema(description = "Nome completo do condutor.", example = "Zezin Danonão")
        @NotBlank(message = "O nome do condutor não pode ser vazio.")
        @Pattern(regexp = "(?=.*[a-zA-Z]).{2,}", message = "O nome do condutor deve conter ao menos duas letras.")
        String nomeCondutor
) {}
