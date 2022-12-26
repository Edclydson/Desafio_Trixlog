package com.trix.crud.modelo;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="condutores")
@ApiModel
public class Condutor{
    
    @Id
    @Column(length = 11)
    private String numeroCnh;

    private String nomeCondutor;

    @OneToMany
    private List<Veiculo> listaDeVeiculos;

}
