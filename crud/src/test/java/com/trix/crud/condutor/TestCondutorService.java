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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
//
//    @Test
//    void DeveConsultarCondutorPelaCnh(){
//
//        Mockito.when(repository.findById(ArgumentMatchers.anyString()))
//                .thenReturn(Optional.of(condutor));
//
//        ResponseEntity resultado = service.consultaCondutorcnh(condutor.getNumeroCnh());
//
//        assertNotNull(resultado);
//        assertNotNull(resultado.getBody());
//        assertEquals(condutor,resultado.getBody());
//        assertEquals(HttpStatus.OK,resultado.getStatusCode());
//
//        Mockito.verify(repository,Mockito.times(2)).findById(ArgumentMatchers.anyString());
//    }
//
//
//    @Test
//    void DeveRetornarStatusCodeOK_AoAlterarCondutor(){
//        Condutor condutorAlterado = new Condutor();
//        condutorAlterado.setNomeCondutor("Leidson Melo");
//        condutorAlterado.setNumeroCnh(condutor.getNumeroCnh());
//        condutorAlterado.setListaDeVeiculos(Collections.emptyList());
//
//        Mockito.when(repository.findById(condutorAlterado.getNumeroCnh())).thenReturn(Optional.of(condutorAlterado));
//        Mockito.when(repository.save(condutorAlterado)).thenReturn(condutor);
//
//
//        ResponseEntity resultado = service.alteraCondutor(condutorAlterado);
//        assertEquals(HttpStatus.OK,resultado.getStatusCode());
//        assertEquals("Alterações salvas com sucesso!",resultado.getBody());
//        Mockito.verify(repository).save(ArgumentMatchers.any(Condutor.class));
//    }
//
//    @Test
//    void DeveRetornarStatusCodeOKComErro_AoAlterarCondutor(){
//        Condutor condutorAlterado = new Condutor();
//        condutorAlterado.setNomeCondutor(condutor.getNumeroCnh());
//        condutorAlterado.setNumeroCnh(condutor.getNumeroCnh());
//        condutorAlterado.setListaDeVeiculos(Collections.emptyList());
//
//        Mockito.when(condutorAlterado.getNomeCondutor()).thenReturn(String.valueOf(false));
//
//
//        ResponseEntity resultado = service.alteraCondutor(condutorAlterado);
//        assertEquals(HttpStatus.OK,resultado.getStatusCode());
//        assertEquals("Houve um problema ao salvar as alterações",resultado.getBody());
//
//        condutorAlterado.setNomeCondutor("Le1dson Mel0");
//        resultado = service.alteraCondutor(condutorAlterado);
//        assertEquals(HttpStatus.OK,resultado.getStatusCode());
//        assertEquals("Houve um problema ao salvar as alterações", resultado.getBody());
//    }
//
//    @Test
//    void DeveRetornarStatusCodeNoContent_AoExcluirCondutor(){
//        condutor = new Condutor();
//        condutor.setNomeCondutor("Edclydson Sousa");
//        condutor.setNumeroCnh("77546831291");
//        condutor.setListaDeVeiculos(Collections.emptyList());
//
//        Mockito.when(repository.findById(condutor.getNumeroCnh())).thenReturn(Optional.of(condutor));
//
//        ResponseEntity resultado = service.deletaCondutor(condutor.getNumeroCnh());
//        assertEquals(HttpStatus.NO_CONTENT,resultado.getStatusCode());
//        Mockito.verify(repository, Mockito.times(1)).deleteById(ArgumentMatchers.anyString());
//        Mockito.verify(repository, Mockito.times(1)).findById(ArgumentMatchers.anyString());
//    }
//
//    @Test
//    void DeveRetornarStatusCodeOK_AoExcluirCondutor(){
//        condutor = new Condutor();
//        condutor.setNomeCondutor("Edclydson Sousa");
//        condutor.setNumeroCnh("77546831291");
//        condutor.setListaDeVeiculos(Collections.emptyList());
//
//        Mockito.when(repository.findById(condutor.getNumeroCnh())).thenReturn(Optional.empty());
//
//        ResponseEntity resultado = service.deletaCondutor(condutor.getNumeroCnh());
//        assertEquals(HttpStatus.OK,resultado.getStatusCode());
//        assertEquals("Houve um problema na hora de remover o condutor",resultado.getBody());
//        Mockito.verify(repository).findById(ArgumentMatchers.anyString());
//    }
//
//    @Test
//    void DeveRetornarSucesso_AdquirirVeiculo(){
//        condutor = new Condutor();
//        condutor.setNomeCondutor("Edclydson Sousa");
//        condutor.setNumeroCnh("77546831291");
//        condutor.setListaDeVeiculos(Collections.singletonList(veiculo));
//
//        when(repository.existsById("77546831291")).thenReturn(true);
//        when(repository.findById("77546831291")).thenReturn(Optional.of(condutor));
//        when(veiculo.getCnhCondutor()).thenReturn("");
//        when(veiculoRepository.findById("78461824953")).thenReturn(Optional.of(veiculo));
//        when(repository.save(condutor)).thenReturn(condutor);
//        when(veiculoRepository.save(veiculo)).thenReturn(veiculo);
//
//        ResponseEntity resposta = service.adquirirVeiculo("78461824953","77546831291");
//
//        assertNotNull(resposta);
//        assertEquals(HttpStatus.OK,resposta.getStatusCode());
//        assertEquals("Veículo adquirido com sucesso",resposta.getBody());
//        Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(Condutor.class));
//        Mockito.verify(veiculoRepository, Mockito.times(1)).save(ArgumentMatchers.any(Veiculo.class));
//
//    }
//
//    @Test
//    void DeveRetornarErro_AdquirirVeiculo(){
//        condutor = new Condutor();
//        condutor.setNomeCondutor("Edclydson Sousa");
//        condutor.setNumeroCnh("77546831291");
//        condutor.setListaDeVeiculos(Collections.singletonList(veiculo));
//
//        ResponseEntity resposta = service.adquirirVeiculo("78461824953","7754683129"); // cnh com 10 digitos
//
//        assertNotNull(resposta);
//        assertEquals(HttpStatus.OK,resposta.getStatusCode());
//        assertEquals("Para adquerir um veiculo informe os dados corretamente.",resposta.getBody());
//
//        resposta = service.adquirirVeiculo("7846182495","77546831291"); // renavam com 10 digitos
//
//        assertNotNull(resposta);
//        assertEquals(HttpStatus.OK,resposta.getStatusCode());
//        assertEquals("Para adquerir um veiculo informe os dados corretamente.",resposta.getBody());
//
//        resposta = service.adquirirVeiculo("78461824953","7754683129l"); // letra na cnh
//
//        assertNotNull(resposta);
//        assertEquals(HttpStatus.OK,resposta.getStatusCode());
//        assertEquals("Para adquerir um veiculo informe os dados corretamente.",resposta.getBody());
//
//        resposta = service.adquirirVeiculo("7846l824953","77546831291"); // letra no renavam
//
//        assertNotNull(resposta);
//        assertEquals(HttpStatus.OK,resposta.getStatusCode());
//        assertEquals("Para adquerir um veiculo informe os dados corretamente.",resposta.getBody());
//
//        resposta = service.adquirirVeiculo("-78461824953","77546831291"); // numero negativo no renavam
//
//        assertNotNull(resposta);
//        assertEquals(HttpStatus.OK,resposta.getStatusCode());
//        assertEquals("Para adquerir um veiculo informe os dados corretamente.",resposta.getBody());
//
//        resposta = service.adquirirVeiculo("00000000000","77546831291"); // 11 digitos 0 no renavam
//
//        assertNotNull(resposta);
//        assertEquals(HttpStatus.OK,resposta.getStatusCode());
//        assertEquals("Para adquerir um veiculo informe os dados corretamente.",resposta.getBody());
//
//
//    }
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
//    @Test
//    void DeveRetornarStatusCodeOK_AoBuscarCondutorPeloNome(){
//        condutor = new Condutor();
//        condutor.setNomeCondutor("Zé Bedeu");
//        condutor.setNumeroCnh("77546831291");
//        condutor.setListaDeVeiculos(Collections.emptyList());
//        lista.add(condutor);
//
//        when(repository.findByNomeCondutor(condutor.getNomeCondutor())).thenReturn(lista);
//
//        ResponseEntity resultado = service.buscaNomeCondutor("Zé Bedeu");
//        assertNotNull(resultado);
//        assertEquals(lista, resultado.getBody());
//        Mockito.verify(repository, Mockito.times(1)).findByNomeCondutor(ArgumentMatchers.anyString());
//
//
//        when(repository.findByNomeCondutorContaining("Zé")).thenReturn(lista);
//
//        resultado = service.buscaNomeCondutor("Zé");
//        assertNotNull(resultado);
//        assertEquals(lista, resultado.getBody());
//        Mockito.verify(repository, Mockito.times(1)).findByNomeCondutorContaining(ArgumentMatchers.anyString());
//    }
//
//    @Test
//    void DeveRetornarStatusCodeOKComErro_AoBuscarCondutorPeloNome(){
//        ResponseEntity resultado = service.buscaNomeCondutor("Zé Bede1");
//
//        assertNotNull(resultado);
//        assertEquals(HttpStatus.OK, resultado.getStatusCode());
//        assertEquals("Nome inválido!", resultado.getBody());
//    }
//

}
