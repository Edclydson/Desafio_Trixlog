package com.api.condutor.condutor;

import com.api.condutor.dto.CondutorRequest;
import com.api.condutor.dto.CondutorResponse;
import com.api.condutor.mapper.CondutorMapper;
import com.api.condutor.modelo.Condutor;
import com.api.condutor.repository.CondutorRepository;
import com.api.condutor.service.CondutorService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestCondutorService {
    
    @Mock
    CondutorRepository repository;
    @Mock
    CondutorMapper mapper;
    
    @InjectMocks
    CondutorService condutorService;

    private Condutor condutor;
    private CondutorRequest request;
    private CondutorResponse response;
    private List<Condutor> lista = new ArrayList<>();

    @BeforeEach
    void init(){
        condutor = new Condutor();
        condutor.setNumeroCnh("77546831291");
        condutor.setNomeCondutor("Zé Bedeu");
        condutor.setRenavams(new ArrayList<>());
        request = new CondutorRequest("77546831291", "Zé Bedeu");
        response = new CondutorResponse("77546831291", "Zé Bedeu", new ArrayList<>());
    }

    @Test
    void DeveRetornarResponse_AoCadastrarNovoCondutor(){
        Mockito.when(repository.existsById(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(mapper.toEntity(ArgumentMatchers.any(CondutorRequest.class))).thenReturn(condutor);
        Mockito.when(repository.save(ArgumentMatchers.any(Condutor.class))).thenReturn(condutor);
        Mockito.when(mapper.toResponse(ArgumentMatchers.any(Condutor.class))).thenReturn(response);
        
        CondutorResponse resultado = condutorService.cadastraNovoCondutor(request);
        assertNotNull(resultado);
        assertEquals("77546831291", resultado.numeroCnh());
        Mockito.verify(repository).save(ArgumentMatchers.any(Condutor.class));
    }

    @Test
    void DeveLancarException_AoCadastrarCondutorDuplicado(){
        Mockito.when(repository.existsById(ArgumentMatchers.anyString())).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> condutorService.cadastraNovoCondutor(request));
    }

    @Test
    void DeveListarTodosCondutores(){
        lista.add(condutor);
        Mockito.when(repository.findAll()).thenReturn(lista);
        Mockito.when(mapper.toResponse(ArgumentMatchers.any(Condutor.class))).thenReturn(response);
        List<CondutorResponse> retorno = condutorService.consultaTodosCondutores();
        assertEquals(1, retorno.size());
    }

    @Test
    void DeveRetornarSucesso_AoConsultarCondutorPelaCnh(){
        Mockito.when(repository.findById(ArgumentMatchers.anyString())).thenReturn(Optional.of(condutor));
        Mockito.when(mapper.toResponse(ArgumentMatchers.any(Condutor.class))).thenReturn(response);
        CondutorResponse resultado = condutorService.consultaCondutorcnh("77546831291");
        assertNotNull(resultado);
        assertEquals(condutor.getNumeroCnh(), resultado.numeroCnh());
    }

    @Test
    void DeveLancarException_AoConsultarCondutorInexistente(){
        Mockito.when(repository.findById(ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> condutorService.consultaCondutorcnh("123"));
    }

    @Test
    void DeveRetornarResponse_AoAlterarCondutor(){
        Mockito.when(repository.findById(ArgumentMatchers.anyString())).thenReturn(Optional.of(condutor));
        Mockito.when(mapper.toResponse(ArgumentMatchers.any(Condutor.class))).thenReturn(response);

        CondutorResponse resultado = condutorService.alteraCondutor(request);
        assertNotNull(resultado);
        // Save não é mais chamado explicitamente devido ao Dirty Checking
        Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any(Condutor.class));
    }

    @Test
    void DeveRetornarNoContent_AoExcluirCondutor(){
        Mockito.when(repository.existsById(ArgumentMatchers.anyString())).thenReturn(true);
        condutorService.deletaCondutor(condutor.getNumeroCnh());
        Mockito.verify(repository).deleteById(ArgumentMatchers.anyString());
    }

    @Test
    void DeveLancarException_AoExcluirCondutorInexistente(){
        Mockito.when(repository.existsById(ArgumentMatchers.anyString())).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> condutorService.deletaCondutor(condutor.getNumeroCnh()));
    }
}
