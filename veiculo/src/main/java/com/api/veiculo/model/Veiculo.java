package com.api.veiculo.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade que representa um Veículo no sistema.
 */
@Entity
@Table(name = "veiculo")
@Schema(description = "Entidade que representa um Veículo")
public class Veiculo {
    @Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Schema(description = "ID único gerado para o veículo")
    private UUID veiculoId;

    @Column(name = "renavam_veiculo", length = 11)
    @Schema(description = "Renavam do veículo, com 11 dígitos", example = "12345678901")
    private String renavam;
    
    @Column(length = 7, unique = true)
    @Schema(description = "Placa do veículo", example = "ABC1234")
    private String placa;
    
    @Column(length = 17)
    @Schema(description = "Chassi do veículo", example = "9BWZZZ37ZKT000000")
    private String chassi;
    
    @Column(length = 4)
    @Schema(description = "Ano do modelo", example = "2023")
    private String anoModelo;
    
    @Column(length = 4)
    @Schema(description = "Ano de fabricação", example = "2023")
    private String anoFabricacao;
    
    @Column(length = 20)
    @Schema(description = "Cor predominante", example = "Prata")
    private String cor;
    
    @Column(length = 2)
    @Schema(description = "UF da placa do veículo", example = "SP")
    private String ufPlaca;
    
    @Column(nullable = false, columnDefinition = "DATE")
    @Schema(description = "Data em que o veículo foi adquirido")
    private LocalDate dataAquisicao;
    
    @Column(length = 11)
    @Schema(description = "CNH do condutor alocado", example = "12345678901")
    private String cnhCondutor;

    public String getRenavam() {
        return renavam;
    }

    public void setRenavam(String renavam) {
        this.renavam = renavam;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getChassi() {
        return chassi;
    }

    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    public String getAnoModelo() {
        return anoModelo;
    }

    public void setAnoModelo(String anoModelo) {
        this.anoModelo = anoModelo;
    }

    public String getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(String anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getUfPlaca() {
        return ufPlaca;
    }

    public void setUfPlaca(String ufPlaca) {
        this.ufPlaca = ufPlaca;
    }

    public LocalDate getDataAquisicao() {
        return dataAquisicao;
    }

    public void setDataAquisicao(LocalDate dataAquisicao) {
        this.dataAquisicao = dataAquisicao;
    }

    public String getCnhCondutor() {
        return cnhCondutor;
    }

    public void setCnhCondutor(String cnhCondutor) {
        this.cnhCondutor = cnhCondutor;
    }
}
