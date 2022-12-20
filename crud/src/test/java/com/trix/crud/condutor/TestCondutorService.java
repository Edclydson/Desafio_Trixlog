package com.trix.crud.condutor;

import com.trix.crud.ApplicationConfigTest;
import com.trix.crud.dto.NovoCondutor;
import com.trix.crud.modelo.Condutor;
import com.trix.crud.repository.CondutorRepository;
import com.trix.crud.service.CondutorService;
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

import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class TestCondutorService extends ApplicationConfigTest {

    @MockBean
    CondutorRepository repository;

    @Autowired
    CondutorService service;

    private Condutor condutor;

    private NovoCondutor novoCondutor;

    private List<Condutor> lista = new ArrayList<>();

    @BeforeEach
    void setup(){
        standaloneSetup(this.service);

        condutor = mock(Condutor.class);
        when(condutor.getNomeCondutor()).thenReturn("Josney");
        when(condutor.getNumeroCnh()).thenReturn("77546831291");
        when(condutor.getListaDeVeiculos()).thenReturn(Collections.emptyList());

        novoCondutor = mock(NovoCondutor.class);
        when(novoCondutor.getNome()).thenReturn("Zé Bedeu");
        when(novoCondutor.getNumCnh()).thenReturn("79541234658");
    }

    @Test
    void DeveRetornarTrue_AoCadastrarNovoCondutor(){
        novoCondutor = new NovoCondutor();
        novoCondutor.setNome("Edclydson Sousa");
        novoCondutor.setNumCnh("77546831291");

        Mockito.when(repository.save(condutor)).thenReturn(condutor);

        boolean resultado = service.cadastraNovoCondutor(novoCondutor);
        assertTrue(resultado);
        verify(repository, times(1)).save(ArgumentMatchers.any(Condutor.class));
    }

    @Test
    void DeveRetornarFalse_AoCadastrarNovoCondutor(){
        novoCondutor = new NovoCondutor();
        novoCondutor.setNome("Edclydson Sousa");
        novoCondutor.setNumCnh("7546831291");

        boolean resultado = service.cadastraNovoCondutor(novoCondutor);
        assertFalse(resultado);

        novoCondutor.setNome("77546831291");
        novoCondutor.setNumCnh("Edclydson Sousa");

        resultado = service.cadastraNovoCondutor(novoCondutor);
        assertFalse(resultado);

        novoCondutor.setNome("Edc1yds0n S0usa");
        novoCondutor.setNumCnh("77546831291");

        resultado = service.cadastraNovoCondutor(novoCondutor);
        assertFalse(resultado);

        novoCondutor.setNome("Edclydson Sousa");
        novoCondutor.setNumCnh("775A683l29l");

        resultado = service.cadastraNovoCondutor(novoCondutor);
        assertFalse(resultado);
    }

    @Test
    void DeveListarTodosCondutores() {
        lista.add(condutor);
        Mockito.when(repository.findAll()).thenReturn(lista);

        List<Condutor> resultado = service.consultaTodosCondutores();

        assertNotNull(resultado);
        assertEquals(ArrayList.class,resultado.getClass());
        Mockito.verify(repository).findAll();
    }

    @Test
    void DeveConsultarCondutorPelaCnh(){

        Mockito.when(repository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(condutor));

        ResponseEntity resultado = service.consultaCondutorcnh(condutor.getNumeroCnh());

        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals(condutor,resultado.getBody());
        assertEquals(HttpStatus.OK,resultado.getStatusCode());

        Mockito.verify(repository,Mockito.times(2)).findById(ArgumentMatchers.anyString());
    }


    @Test
    void DeveRetornarStatusCodeOK_AoAlterarCondutor(){
        Condutor condutorAlterado = new Condutor();
        condutorAlterado.setNomeCondutor("Leidson Melo");
        condutorAlterado.setNumeroCnh(condutor.getNumeroCnh());
        condutorAlterado.setListaDeVeiculos(Collections.emptyList());

        Mockito.when(repository.findById(condutorAlterado.getNumeroCnh())).thenReturn(Optional.of(condutorAlterado));
        Mockito.when(repository.save(condutorAlterado)).thenReturn(condutor);


        ResponseEntity resultado = service.alteraCondutor(condutorAlterado);
        assertEquals(HttpStatus.OK,resultado.getStatusCode());
        assertEquals("Alterações salvas com sucesso!",resultado.getBody());
        Mockito.verify(repository).save(ArgumentMatchers.any(Condutor.class));
    }

    @Test
    void DeveRetornarStatusCodeOKComErro_AoAlterarCondutor(){
        Condutor condutorAlterado = new Condutor();
        condutorAlterado.setNomeCondutor(condutor.getNumeroCnh());
        condutorAlterado.setNumeroCnh(condutor.getNumeroCnh());
        condutorAlterado.setListaDeVeiculos(Collections.emptyList());

        Mockito.when(condutorAlterado.getNomeCondutor()).thenReturn(String.valueOf(false));


        ResponseEntity resultado = service.alteraCondutor(condutorAlterado);
        assertEquals(HttpStatus.OK,resultado.getStatusCode());
        assertEquals("Houve um problema ao salvar as alterações",resultado.getBody());

        condutorAlterado.setNomeCondutor("Le1dson Mel0");
        resultado = service.alteraCondutor(condutorAlterado);
        assertEquals(HttpStatus.OK,resultado.getStatusCode());
        assertEquals("Houve um problema ao salvar as alterações", resultado.getBody());
    }

    @Test
    void DeveRetornarStatusCodeNoContent_AoExcluirCondutor(){
        condutor = new Condutor();
        condutor.setNomeCondutor("Edclydson Sousa");
        condutor.setNumeroCnh("77546831291");
        condutor.setListaDeVeiculos(Collections.emptyList());

        Mockito.when(repository.findById(condutor.getNumeroCnh())).thenReturn(Optional.of(condutor));

        ResponseEntity resultado = service.deletaCondutor(condutor.getNumeroCnh());
        assertEquals(HttpStatus.NO_CONTENT,resultado.getStatusCode());
        Mockito.verify(repository, Mockito.times(1)).deleteById(ArgumentMatchers.anyString());
        Mockito.verify(repository, Mockito.times(1)).findById(ArgumentMatchers.anyString());
    }

    @Test
    void DeveRetornarStatusCodeOK_AoExcluirCondutor(){
        condutor = new Condutor();
        condutor.setNomeCondutor("Edclydson Sousa");
        condutor.setNumeroCnh("77546831291");
        condutor.setListaDeVeiculos(Collections.emptyList());

        Mockito.when(repository.findById(condutor.getNumeroCnh())).thenReturn(Optional.empty());

        ResponseEntity resultado = service.deletaCondutor(condutor.getNumeroCnh());
        assertEquals(HttpStatus.OK,resultado.getStatusCode());
        assertEquals("Houve um problema na hora de remover o condutor",resultado.getBody());
        Mockito.verify(repository).findById(ArgumentMatchers.anyString());
    }

    @Test
    void DeveAddVeiculoAoCondutor(){
        service.addVeiculoCondutor(condutor);
        Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(Condutor.class));
    }

    @Test
    void DeveRemoverVeiculoDoCondutor(){
        service.rmvVeiculoCondutor(condutor);
        Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(Condutor.class));
    }

    @Test
    void DeveRetornarStatusCodeOK_AoBuscarCondutorPeloNome(){
        condutor = new Condutor();
        condutor.setNomeCondutor("Zé Bedeu");
        condutor.setNumeroCnh("77546831291");
        condutor.setListaDeVeiculos(Collections.emptyList());
        lista.add(condutor);

        when(repository.findByNomeCondutor(condutor.getNomeCondutor())).thenReturn(lista);

        ResponseEntity resultado = service.buscaNomeCondutor("Zé Bedeu");
        assertNotNull(resultado);
        assertEquals(lista, resultado.getBody());
        Mockito.verify(repository, Mockito.times(1)).findByNomeCondutor(ArgumentMatchers.anyString());


        when(repository.findByNomeCondutorContaining("Zé")).thenReturn(lista);

        resultado = service.buscaNomeCondutor("Zé");
        assertNotNull(resultado);
        assertEquals(lista, resultado.getBody());
        Mockito.verify(repository, Mockito.times(1)).findByNomeCondutorContaining(ArgumentMatchers.anyString());
    }

    @Test
    void DeveRetornarStatusCodeOKComErro_AoBuscarCondutorPeloNome(){
        ResponseEntity resultado = service.buscaNomeCondutor("Zé Bede1");

        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals("Nome inválido!", resultado.getBody());
    }


}
