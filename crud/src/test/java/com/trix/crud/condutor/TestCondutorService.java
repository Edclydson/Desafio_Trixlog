package com.trix.crud.condutor;

import com.trix.crud.ApplicationConfigTest;
import com.trix.crud.dto.NewDriver;
import com.trix.crud.modelo.Condutor;
import com.trix.crud.repository.CondutorRepository;
import com.trix.crud.service.CondutorAcoes;
import com.trix.crud.service.CondutorService;
import com.trix.crud.service.CondutorValidacoes;
import com.trix.crud.service.VeiculoValidacoes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class TestCondutorService extends ApplicationConfigTest {
    @MockBean
    CondutorRepository repository;
    @MockBean
    CondutorValidacoes valida;
    @MockBean
    CondutorAcoes acao;
    @MockBean
    VeiculoValidacoes veiculoValidacoes;
    @Autowired
    CondutorService condutorService;

    private Condutor condutor;

    private NewDriver novoCondutor;

    private List<Condutor> lista = new ArrayList<>();

    @BeforeEach
    void setup(){

        condutor = mock(Condutor.class);
        when(condutor.getNomeCondutor()).thenReturn("Josney");
        when(condutor.getNumeroCnh()).thenReturn("77546831291");
        when(condutor.getListaDeVeiculos()).thenReturn(Collections.emptyList());

        novoCondutor = mock(NewDriver.class);
        when(novoCondutor.nameDriver()).thenReturn("Zé Bedeu");
        when(novoCondutor.cnhNumber()).thenReturn("79541234658");

    }

    @Test
    void DeveRetornarTrue_AoCadastrarNovoCondutor(){
        Mockito.when(valida.cnhValida(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(valida.nomeCondutor(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(acao.geraCondutor(novoCondutor)).thenReturn(condutor);
        Mockito.when(repository.save(acao.geraCondutor(novoCondutor))).thenReturn(condutor);

        boolean resultado = condutorService.cadastraNovoCondutor(novoCondutor);

        assertTrue(resultado);
        verify(repository, times(1)).save(ArgumentMatchers.any(Condutor.class));
    }

    @Test
    void DeveRetornarFalse_AoCadastrarNovoCondutor(){
        Mockito.when(valida.cnhValida(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(valida.nomeCondutor(ArgumentMatchers.anyString())).thenReturn(true);
        //cnh inválida

        boolean resultado = condutorService.cadastraNovoCondutor(novoCondutor);
        assertFalse(resultado);

        Mockito.when(valida.cnhValida(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(valida.nomeCondutor(ArgumentMatchers.anyString())).thenReturn(false);
        //nome invalido

        resultado = condutorService.cadastraNovoCondutor(novoCondutor);
        assertFalse(resultado);
    }

    @Test
    void DeveListarTodosCondutores() {
        lista.add(condutor);
        Mockito.when(repository.findAll()).thenReturn(lista);

        List<Condutor> resultado = condutorService.consultaTodosCondutores();

        assertNotNull(resultado);
        assertEquals(ArrayList.class,resultado.getClass());
        Mockito.verify(repository).findAll();
    }

    @Test
    void DeveRetornarSucesso_AoConsultarCondutorPelaCnh(){

        Mockito.when(repository.findById(condutor.getNumeroCnh()))
                .thenReturn(Optional.of(condutor));
        Mockito.when(valida.cnhValida(ArgumentMatchers.anyString())).thenReturn(true);

        ResponseEntity resultado = condutorService.consultaCondutorcnh(condutor.getNumeroCnh());

        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals(Condutor.class,resultado.getBody().getClass());
        assertEquals(HttpStatus.OK,resultado.getStatusCode());

        Mockito.verify(repository).findById(ArgumentMatchers.anyString());
    }

    @Test
    void DeveRetornarFalha_AoConsultarCondutorPelaCnh(){
        Mockito.when(valida.cnhValida(ArgumentMatchers.anyString())).thenReturn(false);

        ResponseEntity resultado = condutorService.consultaCondutorcnh(condutor.getNumeroCnh());

        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals(HttpStatus.OK,resultado.getStatusCode());

        Mockito.when(valida.cnhValida(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(repository.findById(ArgumentMatchers.anyString())).thenReturn(Optional.empty());

        resultado = condutorService.consultaCondutorcnh(condutor.getNumeroCnh());

        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals("Condutor não encontrado!",resultado.getBody());
        assertEquals(HttpStatus.OK,resultado.getStatusCode());
    }

    @Test
    void DeveRetornarStatusCodeOK_AoAlterarCondutor(){

        Mockito.when(valida.nomeCondutor(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(valida.existe(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(acao.alteracaoCondutor(condutor)).thenReturn(condutor);

        ResponseEntity resultado = condutorService.alteraCondutor(condutor);

        assertEquals(HttpStatus.OK,resultado.getStatusCode());
        assertEquals("Alterações salvas com sucesso!",resultado.getBody());
        Mockito.verify(repository).save(ArgumentMatchers.any(Condutor.class));
    }

    @Test
    void DeveRetornarStatusCodeOKComErro_AoAlterarCondutor(){

        Mockito.when(valida.nomeCondutor(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(valida.existe(ArgumentMatchers.anyString())).thenReturn(true);

        ResponseEntity resultado = condutorService.alteraCondutor(condutor);

        assertEquals(HttpStatus.OK,resultado.getStatusCode());
        assertEquals("Houve um problema ao salvar as alterações",resultado.getBody());


        Mockito.when(valida.nomeCondutor(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(valida.existe(ArgumentMatchers.anyString())).thenReturn(false);

        resultado = condutorService.alteraCondutor(condutor);

        assertEquals(HttpStatus.OK,resultado.getStatusCode());
        assertEquals("Houve um problema ao salvar as alterações",resultado.getBody());
    }

    @Test
    void DeveRetornarNoContent_AoExcluirCondutor(){

        Mockito.when(valida.cnhValida(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(valida.existe(ArgumentMatchers.anyString())).thenReturn(true);

        ResponseEntity resultado = condutorService.deletaCondutor(condutor.getNumeroCnh());

        assertEquals(HttpStatus.NO_CONTENT,resultado.getStatusCode());
        Mockito.verify(repository).deleteById(ArgumentMatchers.anyString());
    }

    @Test
    void DeveRetornarOK_AoExcluirCondutor(){

        Mockito.when(valida.cnhValida(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(valida.existe(ArgumentMatchers.anyString())).thenReturn(true);

        ResponseEntity resultado = condutorService.deletaCondutor(condutor.getNumeroCnh());

        assertEquals(HttpStatus.OK,resultado.getStatusCode());


        Mockito.when(valida.cnhValida(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(valida.existe(ArgumentMatchers.anyString())).thenReturn(false);

        resultado = condutorService.deletaCondutor(condutor.getNumeroCnh());

        assertEquals(HttpStatus.OK,resultado.getStatusCode());
    }

    @Test
    void DeveRetornarOK_AoBuscarCondutorPeloNome(){
        lista.add(condutor);

        Mockito.when(valida.nomeCondutor(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(repository.findByNomeCondutor(ArgumentMatchers.anyString())).thenReturn(lista);

        ResponseEntity resultado = condutorService.buscaNomeCondutor("Zé Bedeu");
        assertNotNull(resultado);
        assertEquals(lista, resultado.getBody());
        Mockito.verify(repository, Mockito.times(1)).findByNomeCondutor(ArgumentMatchers.anyString());


        Mockito.when(repository.findByNomeCondutorContaining(ArgumentMatchers.anyString())).thenReturn(lista);

        resultado = condutorService.buscaNomeCondutor("Zé");
        assertNotNull(resultado);
        assertEquals(lista, resultado.getBody());
        Mockito.verify(repository, Mockito.times(1)).findByNomeCondutorContaining(ArgumentMatchers.anyString());
    }

    @Test
    void DeveRetornarOKComErro_AoBuscarCondutorPeloNome(){
        Mockito.when(valida.nomeCondutor(ArgumentMatchers.anyString())).thenReturn(false);

        ResponseEntity resultado = condutorService.buscaNomeCondutor("Zé Bede1");

        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals("Nome inválido!", resultado.getBody());
    }


    @Test
    void DeveRetornarOKComSucesso_AoAdquirirVeiculo(){

        Mockito.when(veiculoValidacoes.requisitosAquisicaoVeiculo(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(valida.requisitosAquisicaoVeiculo(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(acao.acquireProcess(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenReturn(condutor);

        ResponseEntity resposta = condutorService.adquirirVeiculo("78461824953","77546831291");

        assertNotNull(resposta);
        assertEquals(HttpStatus.OK,resposta.getStatusCode());
        assertEquals("Veículo adquirido com sucesso",resposta.getBody());
        Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(Condutor.class));

    }

    @Test
    void DeveRetornarOKComErro_AdquirirVeiculo(){

        Mockito.when(veiculoValidacoes.requisitosAquisicaoVeiculo(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(valida.requisitosAquisicaoVeiculo(ArgumentMatchers.anyString())).thenReturn(false);

        ResponseEntity resposta = condutorService.adquirirVeiculo("78461824953","7754683129"); // cnh com 10 digitos

        assertNotNull(resposta);
        assertEquals(HttpStatus.OK,resposta.getStatusCode());
        assertEquals("Para adquerir um veiculo informe os dados corretamente.",resposta.getBody());


        Mockito.when(veiculoValidacoes.requisitosAquisicaoVeiculo(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(valida.requisitosAquisicaoVeiculo(ArgumentMatchers.anyString())).thenReturn(true);

        resposta = condutorService.adquirirVeiculo("7846182495","7754683129"); // renavam com 10 digitos

        assertNotNull(resposta);
        assertEquals(HttpStatus.OK,resposta.getStatusCode());
        assertEquals("Para adquerir um veiculo informe os dados corretamente.",resposta.getBody());

    }

    @Test
    void DeveRetornarOKComSucesso_AoliberarVeiculo(){

        Mockito.when(acao.loseProcess(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenReturn(condutor);
        Mockito.when(valida.temAlgumVeiculo(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(valida.possuiOVeiculo(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(valida.cnhValida(ArgumentMatchers.anyString())).thenReturn(true);

        ResponseEntity resposta = condutorService.liberarVeiculo("79541234658","77546831291");

        assertNotNull(resposta);
        assertEquals(HttpStatus.OK,resposta.getStatusCode());
        assertEquals("O Condutor não tem mais posse do veículo: 79541234658",resposta.getBody());
        Mockito.verify(repository).save(ArgumentMatchers.any(Condutor.class));
    }

    @Test
    void DeveRetornarOKComErro_AoLiberaVeiculo(){
        Mockito.when(valida.temAlgumVeiculo(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(valida.possuiOVeiculo(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(valida.cnhValida(ArgumentMatchers.anyString())).thenReturn(false);
        //chn não valida

        ResponseEntity resposta = condutorService.liberarVeiculo("79541234658","77546831291");

        assertNotNull(resposta);
        assertEquals(HttpStatus.OK,resposta.getStatusCode());
        assertEquals("Requisição não foi processada! Tente novamente.",resposta.getBody());

        Mockito.when(valida.temAlgumVeiculo(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(valida.possuiOVeiculo(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(valida.cnhValida(ArgumentMatchers.anyString())).thenReturn(true);
        //condutor não possui o veiculo

        resposta = condutorService.liberarVeiculo("79541234658","77546831291");

        assertNotNull(resposta);
        assertEquals(HttpStatus.OK,resposta.getStatusCode());
        assertEquals("Requisição não foi processada! Tente novamente.",resposta.getBody());

        Mockito.when(valida.temAlgumVeiculo(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(valida.possuiOVeiculo(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(valida.cnhValida(ArgumentMatchers.anyString())).thenReturn(true);
        //condutor não possui nenhum veiculo

        resposta = condutorService.liberarVeiculo("79541234658","77546831291");

        assertNotNull(resposta);
        assertEquals(HttpStatus.OK,resposta.getStatusCode());
        assertEquals("Requisição não foi processada! Tente novamente.",resposta.getBody());

    }

}
