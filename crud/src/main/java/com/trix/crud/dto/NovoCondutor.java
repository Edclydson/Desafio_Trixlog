package com.trix.crud.dto;

import java.util.List;

import com.trix.crud.modelo.Veiculo;


public class NovoCondutor {
    private String nome;
    private String numCnh;

    private List<Veiculo> lista;
    
    public List<Veiculo> getLista() {
        return lista;
    }
    public void setLista(List<Veiculo> lista) {
        this.lista = lista;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getNumCnh() {
        return numCnh;
    }
    public void setNumCnh(String numCnh) {
        this.numCnh = numCnh;
    }
}
