package com.trix.crud.modelo;


import io.swagger.annotations.ApiModel;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name="condutores")
@ApiModel
public class Condutor{
    
    @Id
    @Column(length = 11)
    private String numeroCnh;

    private String nomeCondutor;

    public String getNumeroCnh() {
        return numeroCnh;
    }

    public void setNumeroCnh(String numeroCnh) {
        this.numeroCnh = numeroCnh;
    }

    public String getNomeCondutor() {
        return nomeCondutor;
    }

    public void setNomeCondutor(String nomeCondutor) {
        this.nomeCondutor = nomeCondutor;
    }

    public List<Veiculo> getListaDeVeiculos() {
        return listaDeVeiculos;
    }

    public void setListaDeVeiculos(List<Veiculo> listaDeVeiculos) {
        this.listaDeVeiculos = listaDeVeiculos;
    }

    @OneToMany
    private List<Veiculo> listaDeVeiculos;

}
