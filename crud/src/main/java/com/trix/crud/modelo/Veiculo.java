package com.trix.crud.modelo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="veiculo")
@ApiModel
public class Veiculo {

    @Id
    @Column(name = "renavam_veiculo", length = 11)
    private String renavam;

    @Column(length = 7, unique = true)
    private String placa;
    @Column(length = 17, unique = true)
    private String chassi;
    @Column(length = 4)
    private String anoModelo;
    @Column(length = 4)
    private String anoFabricacao;
    @Column(length = 10)
    private String cor;
    @Column(length = 2)
    private String ufPlaca;
    @Column(nullable = false ,columnDefinition = "DATE")
    private Date dataAquisicao;
    @Column(length = 11)
    private String cnhCondutor;

}
