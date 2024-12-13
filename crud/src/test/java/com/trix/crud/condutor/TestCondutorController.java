package com.trix.crud.condutor;

import com.trix.crud.ApplicationConfigTest;
import com.trix.crud.controller.CondutorController;
import com.trix.crud.dto.NewDriver;
import com.trix.crud.modelo.Condutor;
import com.trix.crud.service.CondutorService;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.junit.jupiter.api.Assertions.*;

class TestCondutorController extends ApplicationConfigTest{

    @Mock
    private CondutorService service;

    @Autowired
    private CondutorController controller;

    private Condutor condutor;

    private List<Condutor> lista = new ArrayList<>();

    private @Valid NewDriver novocondutor;

    @BeforeEach
    void setup(){
        standaloneSetup(this.controller);

        condutor = Mockito.mock(Condutor.class);
        Mockito.when(condutor.getNomeCondutor()).thenReturn("Juca Bala");
        Mockito.when(condutor.getNumeroCnh()).thenReturn("78354291376");
        Mockito.when(condutor.getListaDeVeiculos()).thenReturn(Collections.emptyList());

        novocondutor = Mockito.mock(NewDriver.class);
        Mockito.when(novocondutor.nameDriver()).thenReturn("Biro biro");
        Mockito.when(novocondutor.cnhNumber()).thenReturn("42587169331");
    }

    @Test
    void DeveRetornarSucesso_QuandoListarTodosCondutores() {

        Mockito.when(service.consultaTodosCondutores()).thenReturn(Collections.singletonList(condutor));

        ResponseEntity resultado = controller.listaCondutores();
        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals(HttpStatus.OK,resultado.getStatusCode());
        assertEquals(ResponseEntity.class,resultado.getClass());

        Mockito.verify(service,Mockito.times(1)).consultaTodosCondutores();
    }

    @Test
    void DeveRetornarSucesso_QuandoListarUmCondutor() {
        Mockito.when(service.consultaCondutorcnh(ArgumentMatchers.anyString()))
                .thenReturn(ResponseEntity.ok(Optional.of(condutor)));

        ResponseEntity resultado = controller.buscaCondutor(condutor.getNumeroCnh());
        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals(ResponseEntity.class,resultado.getClass());
        assertEquals(Optional.class,resultado.getBody().getClass());
        assertEquals(HttpStatus.OK,resultado.getStatusCode());

        Mockito.verify(service,Mockito.times(1)).consultaCondutorcnh(ArgumentMatchers.anyString());
    }

    @Test
    void DeveRetornarStatusOKComBodyVazio_QuandoListarUmCondutor() {
        Mockito.when(service.consultaCondutorcnh(ArgumentMatchers.anyString()))
                .thenReturn(ResponseEntity.ok(""));

        ResponseEntity resultado = controller.buscaCondutor(condutor.getNumeroCnh());
        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals("",resultado.getBody());
        assertEquals(String.class,resultado.getBody().getClass());
        assertEquals(ResponseEntity.class,resultado.getClass());
        assertEquals(HttpStatus.OK,resultado.getStatusCode());

        Mockito.verify(service,Mockito.times(1)).consultaCondutorcnh(ArgumentMatchers.anyString());
    }

    @Test
    void DeveRetornarSucesso_AoBuscarNomeCondutor(){
        lista.add(condutor);
        Mockito.when(service.buscaNomeCondutor(ArgumentMatchers.anyString())).thenReturn(ResponseEntity.ok(lista));

        ResponseEntity resultado = controller.buscanomeCondutor(condutor.getNomeCondutor());

        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals(lista, resultado.getBody());
        assertEquals(ArrayList.class, resultado.getBody().getClass());
        assertEquals(ResponseEntity.class, resultado.getClass());
        assertEquals(HttpStatus.OK,resultado.getStatusCode());
        Mockito.verify(service,Mockito.times(1)).buscaNomeCondutor(ArgumentMatchers.anyString());
    }

    @Test
    void DeveRetornarStatusCodeOKComBodyVazio_AoBuscarNomeCondutor(){
        lista.add(condutor);
        Mockito.when(service.buscaNomeCondutor(ArgumentMatchers.anyString())).thenReturn(ResponseEntity.ok(""));

        ResponseEntity resultado = controller.buscanomeCondutor(condutor.getNomeCondutor());

        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertNotEquals(lista, resultado.getBody());
        assertNotEquals(ArrayList.class, resultado.getBody().getClass());
        assertEquals("", resultado.getBody());
        assertEquals(ResponseEntity.class, resultado.getClass());
        assertEquals(HttpStatus.OK,resultado.getStatusCode());
        Mockito.verify(service,Mockito.times(1)).buscaNomeCondutor(ArgumentMatchers.anyString());
    }

    @Test
    void DeveRetornarStatusCodeCreated_AoCadastrarNovoCondutor(){

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        Mockito.when(service.cadastraNovoCondutor(ArgumentMatchers.any(NewDriver.class)))
                .thenReturn(true);

        ResponseEntity resultado = controller.cadastrar(novocondutor,uriBuilder);
        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals("Condutor cadastrado com sucesso!",resultado.getBody());
        assertEquals(HttpStatus.CREATED,resultado.getStatusCode());
        Mockito.verify(service, Mockito.times(1)).cadastraNovoCondutor(ArgumentMatchers.any(NewDriver.class));
    }

    @Test
    void DeveRetornarStatusCodeOK_AoCadastrarNovoCondutor(){

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        Mockito.when(service.cadastraNovoCondutor(ArgumentMatchers.any(NewDriver.class)))
                .thenReturn(false);

        ResponseEntity resultado = controller.cadastrar(novocondutor,uriBuilder);
        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertNotEquals("Condutor cadastrado com sucesso!",resultado.getBody());
        assertNotEquals(HttpStatus.CREATED,resultado.getStatusCode());
        assertEquals("Cadastro não realizado! Verifique os campos e tente novamente.",resultado.getBody());
        assertEquals(HttpStatus.OK,resultado.getStatusCode());
        Mockito.verify(service, Mockito.times(1)).cadastraNovoCondutor(ArgumentMatchers.any(NewDriver.class));
    }

    @Test
    void DeveRetornarSucesso_AoAlterarCondutor(){
        Mockito.when(service.alteraCondutor(ArgumentMatchers.any(Condutor.class)))
                .thenReturn(ResponseEntity.ok("Alterações salvas com sucesso!"));

        ResponseEntity resultado = controller.alterar(condutor);
        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals(HttpStatus.OK,resultado.getStatusCode());
        assertEquals("Alterações salvas com sucesso!",resultado.getBody());

        Mockito.verify(service, Mockito.times(1)).alteraCondutor(ArgumentMatchers.any(Condutor.class));
    }

    @Test
    void DeveRetornarStatusCodeOKComErro_AoAlterarCondutor(){
        Mockito.when(service.alteraCondutor(ArgumentMatchers.any(Condutor.class)))
                .thenReturn(ResponseEntity.ok("Houve um problema ao salvar as alterações"));

        ResponseEntity resultado = controller.alterar(condutor);
        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertNotEquals("Alterações salvas com sucesso!",resultado.getBody());
        assertEquals(HttpStatus.OK,resultado.getStatusCode());
        assertEquals("Houve um problema ao salvar as alterações",resultado.getBody());

        Mockito.verify(service, Mockito.times(1)).alteraCondutor(ArgumentMatchers.any(Condutor.class));
    }

    @Test
    void DeveRetornarStatusCodeNoContent_AoExcluirCondutor(){
        Mockito.when(service.deletaCondutor(ArgumentMatchers.anyString())).thenReturn(ResponseEntity.noContent().build());

        ResponseEntity resultado = controller.deletar(condutor.getNumeroCnh());

        assertEquals(HttpStatus.NO_CONTENT,resultado.getStatusCode());
        assertNotEquals(HttpStatus.OK,resultado.getStatusCode());
        Mockito.verify(service, Mockito.times(1)).deletaCondutor(ArgumentMatchers.anyString());
    }

    @Test
    void DeveRetornarStatusCodeOK_AoExcluirCondutor(){
        Mockito.when(service.deletaCondutor(ArgumentMatchers.anyString()))
                .thenReturn(ResponseEntity.ok("Houve um problema na hora de remover o condutor"));

        ResponseEntity resultado = controller.deletar(condutor.getNumeroCnh());
        assertNotEquals(HttpStatus.NO_CONTENT,resultado.getStatusCode());
        assertEquals(HttpStatus.OK,resultado.getStatusCode());
        Mockito.verify(service, Mockito.times(1)).deletaCondutor(ArgumentMatchers.anyString());
    }

    @Test
    void DeveRetornarStatusCodeOK_AoAdquirirVeiculo(){
        Mockito.when(service.adquirirVeiculo(ArgumentMatchers.anyString(),ArgumentMatchers.anyString()))
                .thenReturn(ResponseEntity.ok("Veículo adquirido com sucesso"));

        ResponseEntity response = controller.addVeiculo("14578652498","25468731845");

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Veículo adquirido com sucesso",response.getBody());

    }

    @Test
    void DeveRetornarStatusCodeOKComErro_AoAdquirirVeiculo(){
        Mockito.when(service.adquirirVeiculo(ArgumentMatchers.anyString(),ArgumentMatchers.anyString()))
                .thenReturn(ResponseEntity.ok("Para adquerir um veiculo informe os dados corretamente."));

        ResponseEntity response = controller.addVeiculo("14578652498","25468731845");

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Para adquerir um veiculo informe os dados corretamente.",response.getBody());

    }

    @Test
    void DeveRetornarStatusCodeOK_AoLiberarVeiculo(){
        Mockito.when(service.liberarVeiculo(ArgumentMatchers.anyString(),ArgumentMatchers.anyString()))
                .thenReturn(ResponseEntity.ok("O Condutor não tem mais posse do veículo: 14578652498"));

        ResponseEntity response = controller.liberarVeiculo("14578652498","25468731845");

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("O Condutor não tem mais posse do veículo: 14578652498",response.getBody());
    }

    @Test
    void DeveRetornarStatusCodeOKComErro_AoLiberarVeiculo(){
        Mockito.when(service.liberarVeiculo(ArgumentMatchers.anyString(),ArgumentMatchers.anyString()))
                .thenReturn(ResponseEntity.ok("Requisição não foi processada! Tente novamente."));

        ResponseEntity response = controller.liberarVeiculo("14578652498","25468731845");

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Requisição não foi processada! Tente novamente.",response.getBody());
    }
}
