package com.trix.crud.condutor;

import com.trix.crud.ApplicationConfigTest;
import com.trix.crud.dto.NovoCondutor;
import com.trix.crud.modelo.Condutor;
import com.trix.crud.modelo.Veiculo;
import com.trix.crud.repository.CondutorRepository;
import com.trix.crud.service.*;
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
    @MockBean
    VeiculoAcoes veiculoAcoes;
    @Autowired
    CondutorService condutorService;

    private Condutor condutor;

    private NovoCondutor novoCondutor;

    private Veiculo veiculo;

    private List<Condutor> lista = new ArrayList<>();

    @BeforeEach
    void setup(){


        condutor = mock(Condutor.class);
        when(condutor.getNomeCondutor()).thenReturn("Josney");
        when(condutor.getNumeroCnh()).thenReturn("77546831291");
        when(condutor.getListaDeVeiculos()).thenReturn(Collections.emptyList());

        novoCondutor = mock(NovoCondutor.class);
        when(novoCondutor.getNome()).thenReturn("Zé Bedeu");
        when(novoCondutor.getNumCnh()).thenReturn("79541234658");

        veiculo = Mockito.mock(Veiculo.class);


    }

    @Test
    void DeveRetornarTrue_AoCadastrarNovoCondutor(){
        novoCondutor = new NovoCondutor();
        novoCondutor.setNome("Edclydson Sousa");
        novoCondutor.setNumCnh("77546831291");

        condutor = new Condutor();
        condutor.setNomeCondutor(novoCondutor.getNome());
        condutor.setNumeroCnh(novoCondutor.getNumCnh());
        condutor.setListaDeVeiculos(Collections.emptyList());

        Mockito.when(valida.cnhValida("77546831291")).thenReturn(true);
        Mockito.when(valida.nomeCondutor("Edclydson Sousa")).thenReturn(true);
        Mockito.when(acao.geraCondutor(novoCondutor)).thenReturn(condutor);
        Mockito.when(repository.save(acao.geraCondutor(novoCondutor))).thenReturn(condutor);

        boolean resultado = condutorService.cadastraNovoCondutor(novoCondutor);
        assertTrue(resultado);
        verify(repository, times(1)).save(ArgumentMatchers.any(Condutor.class));
    }

    @Test
    void DeveRetornarFalse_AoCadastrarNovoCondutor(){
        novoCondutor = new NovoCondutor();
        novoCondutor.setNome("Edclydson Sousa");
        novoCondutor.setNumCnh("7546831291");

        boolean resultado = condutorService.cadastraNovoCondutor(novoCondutor);
        assertFalse(resultado);

        novoCondutor.setNome("77546831291");
        novoCondutor.setNumCnh("Edclydson Sousa");

        resultado = condutorService.cadastraNovoCondutor(novoCondutor);
        assertFalse(resultado);

        novoCondutor.setNome("Edc1yds0n S0usa");
        novoCondutor.setNumCnh("77546831291");

        resultado = condutorService.cadastraNovoCondutor(novoCondutor);
        assertFalse(resultado);

        novoCondutor.setNome("Edclydson Sousa");
        novoCondutor.setNumCnh("775A683l29l");

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
        condutor = new Condutor();
        condutor.setNomeCondutor(novoCondutor.getNome());
        condutor.setNumeroCnh(novoCondutor.getNumCnh());
        condutor.setListaDeVeiculos(Collections.emptyList());

        Mockito.when(repository.findById(condutor.getNumeroCnh()))
                .thenReturn(Optional.of(condutor));
        Mockito.when(valida.cnhValida(condutor.getNumeroCnh())).thenReturn(true);

        ResponseEntity resultado = condutorService.consultaCondutorcnh(condutor.getNumeroCnh());

        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals(Condutor.class,resultado.getBody().getClass());
        assertEquals(HttpStatus.OK,resultado.getStatusCode());

        Mockito.verify(repository).findById(ArgumentMatchers.anyString());
    }

    @Test
    void DeveRetornarFalha_AoConsultarCondutorPelaCnh(){
        Mockito.when(valida.cnhValida(condutor.getNumeroCnh())).thenReturn(false);

        ResponseEntity resultado = condutorService.consultaCondutorcnh(condutor.getNumeroCnh());

        assertEquals(HttpStatus.OK,resultado.getStatusCode());
    }

    @Test
    void DeveRetornarStatusCodeOK_AoAlterarCondutor(){
        Condutor condutorAlterado = new Condutor();
        condutorAlterado.setNomeCondutor("Leidson Melo");
        condutorAlterado.setNumeroCnh(condutor.getNumeroCnh());
        condutorAlterado.setListaDeVeiculos(Collections.emptyList());

        Mockito.when(valida.nomeCondutor(condutorAlterado.getNomeCondutor())).thenReturn(true);
        Mockito.when(valida.existe(condutorAlterado.getNumeroCnh())).thenReturn(true);
        Mockito.when(acao.alteracaoCondutor(condutorAlterado)).thenReturn(condutorAlterado);

        ResponseEntity resultado = condutorService.alteraCondutor(condutorAlterado);

        assertEquals(HttpStatus.OK,resultado.getStatusCode());
        assertEquals("Alterações salvas com sucesso!",resultado.getBody());
        Mockito.verify(repository).save(ArgumentMatchers.any(Condutor.class));
    }

    @Test
    void DeveRetornarStatusCodeOKComErro_AoAlterarCondutor(){
        Condutor condutorAlterado = new Condutor();
        condutorAlterado.setNomeCondutor("Leidson Melo");
        condutorAlterado.setNumeroCnh(condutor.getNumeroCnh());
        condutorAlterado.setListaDeVeiculos(Collections.emptyList());

        Mockito.when(valida.nomeCondutor(condutorAlterado.getNomeCondutor())).thenReturn(false);
        Mockito.when(valida.existe(condutorAlterado.getNumeroCnh())).thenReturn(true);

        ResponseEntity resultado = condutorService.alteraCondutor(condutorAlterado);
        assertEquals(HttpStatus.OK,resultado.getStatusCode());
        assertEquals("Houve um problema ao salvar as alterações",resultado.getBody());

        Mockito.when(valida.nomeCondutor(condutorAlterado.getNomeCondutor())).thenReturn(true);
        Mockito.when(valida.existe(condutorAlterado.getNumeroCnh())).thenReturn(false);

        resultado = condutorService.alteraCondutor(condutorAlterado);
        assertEquals(HttpStatus.OK,resultado.getStatusCode());
        assertEquals("Houve um problema ao salvar as alterações",resultado.getBody());
    }

    @Test
    void DeveRetornarNoContent_AoExcluirCondutor(){

        Mockito.when(valida.cnhValida(condutor.getNumeroCnh())).thenReturn(true);
        Mockito.when(valida.existe(condutor.getNumeroCnh())).thenReturn(true);

        ResponseEntity resultado = condutorService.deletaCondutor(condutor.getNumeroCnh());

        assertEquals(HttpStatus.NO_CONTENT,resultado.getStatusCode());
        Mockito.verify(repository).deleteById(ArgumentMatchers.anyString());
    }

    @Test
    void DeveRetornarOK_AoExcluirCondutor(){

        Mockito.when(valida.cnhValida(condutor.getNumeroCnh())).thenReturn(false);
        Mockito.when(valida.existe(condutor.getNumeroCnh())).thenReturn(true);

        ResponseEntity resultado = condutorService.deletaCondutor(condutor.getNumeroCnh());

        assertEquals(HttpStatus.OK,resultado.getStatusCode());


        Mockito.when(valida.cnhValida(condutor.getNumeroCnh())).thenReturn(true);
        Mockito.when(valida.existe(condutor.getNumeroCnh())).thenReturn(false);

        resultado = condutorService.deletaCondutor(condutor.getNumeroCnh());

        assertEquals(HttpStatus.OK,resultado.getStatusCode());
    }

    @Test
    void DeveRetornarOK_AoBuscarCondutorPeloNome(){
        condutor = new Condutor();
        condutor.setNomeCondutor("Zé Bedeu");
        condutor.setNumeroCnh("77546831291");
        condutor.setListaDeVeiculos(Collections.emptyList());
        lista.add(condutor);

        Mockito.when(valida.nomeCondutor(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(repository.findByNomeCondutor(condutor.getNomeCondutor())).thenReturn(lista);

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
        condutor = new Condutor();
        condutor.setNomeCondutor("Edclydson Sousa");
        condutor.setNumeroCnh("77546831291");
        condutor.setListaDeVeiculos(Collections.singletonList(veiculo));

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
        condutor = new Condutor();
        condutor.setNomeCondutor("Edclydson Sousa");
        condutor.setNumeroCnh("77546831291");
        condutor.setListaDeVeiculos(Collections.singletonList(veiculo));

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
//
//    @Test
//    void DeveRetornarSucesso_AoliberarVeiculo(){
//        veiculo = new Veiculo();
//        veiculo.setRenavam("79541234658");
//        List<Veiculo> listaVeiculos = new ArrayList<>();
//        listaVeiculos.add(veiculo);
//        condutor.setNumeroCnh("77546831291");
//
//        when(repository.findById("77546831291")).thenReturn(Optional.of(condutor));
//        when(Optional.of(condutor).get().getListaDeVeiculos()).thenReturn(listaVeiculos);
//        when(veiculoRepository.findById("79541234658")).thenReturn(Optional.of(veiculo));
//        when(repository.existsById("77546831291")).thenReturn(true);
//
//        ResponseEntity resposta = service.liberarVeiculo("79541234658","77546831291");
//
//        assertNotNull(resposta);
//        assertEquals(HttpStatus.OK,resposta.getStatusCode());
//        assertEquals("O Condutor não tem mais posse do veículo: "+veiculo.getRenavam(),resposta.getBody());
//        Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(Condutor.class));
//    }
//
//    @Test
//    void DeveRetornarErro_AoLiberaVeiculo(){
//        condutor.setNumeroCnh("77546831291");
//
//        //condutor não existente
//        when(repository.findById("77546831291")).thenReturn(Optional.of(condutor));
//        when(repository.existsById("77546831291")).thenReturn(false);
//
//        ResponseEntity resposta = service.liberarVeiculo("79541234658","77546831291");
//
//        assertNotNull(resposta);
//        assertEquals(HttpStatus.OK,resposta.getStatusCode());
//        assertEquals("Requisição não foi processada! Tente novamente.",resposta.getBody());
//
//        //condutor não possui nenhum veiculo
//        when(repository.existsById("77546831291")).thenReturn(true);
//        when(Optional.of(condutor).get().getListaDeVeiculos()).thenReturn(Collections.emptyList());
//
//        resposta = service.liberarVeiculo("79541234658","77546831291");
//
//        assertNotNull(resposta);
//        assertEquals(HttpStatus.OK,resposta.getStatusCode());
//        assertEquals("Requisição não foi processada! Tente novamente.",resposta.getBody());
//
//        //condutor não possui o veiculo informado
//        when(veiculoRepository.findById("79541234658")).thenReturn(Optional.of(veiculo));
//        when(veiculo.getRenavam()).thenReturn("79541234659");
//
//        resposta = service.liberarVeiculo("79541234658","77546831291");
//
//        assertNotNull(resposta);
//        assertEquals(HttpStatus.OK,resposta.getStatusCode());
//        assertEquals("Requisição não foi processada! Tente novamente.",resposta.getBody());
//
//    }
//


}
