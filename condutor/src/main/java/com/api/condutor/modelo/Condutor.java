package com.api.condutor.modelo;


import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name="condutores")
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

    public List<String> getRenavams() {
        return renavams;
    }

    public void setRenavams(List<String> renavams) {
        this.renavams = renavams;
    }

    @ElementCollection
    private List<String> renavams;

}
