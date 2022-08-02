package com.trix.crud.modelo;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="condutores")
public class Condutor {
    
    @Id
    @Column(length = 11)
    private String numeroCnh;

    private String nomeCondutor;

    @OneToMany
    private List<Veiculo> listaDeVeiculos;
    
    public List<Veiculo> getListaDeVeiculos() {
        return listaDeVeiculos;
    }
    public void setListaDeVeiculos(List<Veiculo> listaDeVeiculos) {
        this.listaDeVeiculos = listaDeVeiculos;
    }
    public String getNomeCondutor() {
        return nomeCondutor;
    }
    public void setNomeCondutor(String nomeCondutor) {
        this.nomeCondutor = nomeCondutor;
    }
    public String getNumeroCnh() {
        return numeroCnh;
    }
    public void setNumeroCnh(String numeroCnh) {
        this.numeroCnh = numeroCnh;
    }


}
