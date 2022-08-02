package com.trix.crud.controller.condutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.trix.crud.dto.NovoCondutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.trix.crud.ApplicationConfigTest;
import com.trix.crud.controller.CondutorController;
import com.trix.crud.modelo.Condutor;
import com.trix.crud.service.CondutorService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class TestCondutorController extends ApplicationConfigTest{

    @MockBean
    private CondutorService service;

    @Autowired
    private CondutorController controller;

    private Condutor condutor;

    private Optional<Condutor> condutorOptional;

    private List<Condutor> lista = new ArrayList<>();

    private NovoCondutor novocondutor;


    @BeforeEach
    void setup(){
        condutor = Mockito.mock(Condutor.class);
        Mockito.when(condutor.getNomeCondutor()).thenReturn("Juca Bala");
        Mockito.when(condutor.getNumeroCnh()).thenReturn("78354291376");
        Mockito.when(condutor.getListaDeVeiculos()).thenReturn(Collections.emptyList());

        novocondutor = Mockito.mock(NovoCondutor.class);
        Mockito.when(novocondutor.getNome()).thenReturn("Biro biro");
        Mockito.when(novocondutor.getNumCnh()).thenReturn("42587169331");
        Mockito.when(novocondutor.getLista()).thenReturn(Collections.emptyList());

        condutorOptional = Mockito.mock(Optional.class);
        Mockito.when(condutorOptional.get()).thenReturn(condutor);
    }
    
    @Test
    void DeveListarTodosCondutores(){
        Mockito.when(service.consultaTodosCondutores()).thenReturn(lista);
        lista.add(condutor);
        assertNotNull(controller.listaCondutores());
        assertEquals(lista ,controller.listaCondutores());
        assertEquals(lista.get(0).getNomeCondutor(), controller.listaCondutores().get(0).getNomeCondutor());
        assertEquals(lista.get(0).getNumeroCnh(),controller.listaCondutores().get(0).getNumeroCnh());

    }

    @Test
    void DeveListarUmCondutor(){
        Mockito.when(service.consultaCondutorcnh(condutor.getNumeroCnh())).thenReturn(condutorOptional);

        assertNotNull(controller.buscaCondutor(condutor.getNumeroCnh()));
        assertEquals(condutorOptional, controller.buscaCondutor(condutor.getNumeroCnh()));
        assertEquals(condutorOptional.get().getNumeroCnh(), controller.buscaCondutor(condutor.getNumeroCnh()).get().getNumeroCnh());
        assertEquals(condutorOptional.get().getNomeCondutor(), controller.buscaCondutor(condutor.getNumeroCnh()).get().getNomeCondutor());
    }

    @Test
    void DeveBuscarNomeCondutor(){
        lista.add(condutor);
        Mockito.when(service.buscaNomeCondutor(condutor.getNomeCondutor())).thenReturn(lista);

        assertNotNull(controller.buscanomeCondutor(condutor.getNomeCondutor()));
        assertEquals(lista, controller.buscanomeCondutor(condutor.getNomeCondutor()));
        assertEquals(lista.get(0).getNomeCondutor(), controller.buscanomeCondutor(condutor.getNomeCondutor()).get(0).getNomeCondutor());
    }

    @Test
    void DeveCadastrarNovoCondutor(){
        controller.cadastrar(novocondutor);
        Mockito.verify(service, Mockito.times(1)).cadastraNovoCondutor(ArgumentMatchers.any(NovoCondutor.class));
    }

    @Test
    void DeveAlterarCondutor(){
        controller.alterar(condutor);
        Mockito.verify(service, Mockito.times(1)).alteraCondutor(ArgumentMatchers.any(Condutor.class));
    }

    @Test
    void DeveExcluirCondutor(){
        controller.deletar(condutor.getNumeroCnh());
        Mockito.verify(service, Mockito.times(1)).deletaCondutor(ArgumentMatchers.anyString());
    }
}
