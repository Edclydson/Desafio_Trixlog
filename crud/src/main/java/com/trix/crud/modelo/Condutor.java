package com.trix.crud.modelo;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name="condutores")
public class Condutor {
    
    @Id
    @Column(length = 11)
    private String numeroCnh;

    private String nomeCondutor;

    @OneToMany
    private List<Veiculo> listaDeVeiculos;

}
