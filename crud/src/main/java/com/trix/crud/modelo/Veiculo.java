package com.trix.crud.modelo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
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

}
