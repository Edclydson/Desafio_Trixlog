package com.trix.crud.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="veiculo")
public class Veiculo {

    @Id
    @Column(name = "renavam_veiculo", length = 11)
    private String renavam;

    private String placa;
    @Column(length = 17)
    private String chassi;
    @Column(length = 4)
    private String anoModelo;
    @Column(length = 4)
    private String anoFabricacao;
    private String cor;
    private String ufPlaca;
    private String dataAquisicao;

    private String nomeCondutor;
    private String cnhCondutor;
    
    

    public String getNomeCondutor() {
        return nomeCondutor;
    }
    public void setNomeCondutor(String nomeCondutor) {
        this.nomeCondutor = nomeCondutor;
    }
    public String getCnhCondutor() {
        return cnhCondutor;
    }
    public void setCnhCondutor(String cnhCondutor) {
        this.cnhCondutor = cnhCondutor;
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
    public String getRenavam() {
        return renavam;
    }
    public void setRenavam(String renavam) {
        this.renavam = renavam;
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
    public String getDataAquisicao() {
        return dataAquisicao;
    }
    public void setDataAquisicao(String dataAquisicao) {
        this.dataAquisicao = dataAquisicao;
    }
 
}
