package com.trix.crud.veiculo;

import com.trix.crud.ApplicationConfigTest;
import com.trix.crud.dto.NovoVeiculo;
import com.trix.crud.modelo.Condutor;
import com.trix.crud.modelo.Veiculo;
import com.trix.crud.repository.VeiculoRepository;
import com.trix.crud.service.VeiculoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestVeiculoService extends ApplicationConfigTest {

    @MockBean
    VeiculoRepository repository;

    @Autowired
    VeiculoService service;

    private Veiculo veiculo;

    private NovoVeiculo novoveiculo;

    private Condutor condutor;

    @BeforeEach
    void setup(){
        veiculo = Mockito.mock(Veiculo.class);
        condutor = Mockito.mock(Condutor.class);

        novoveiculo = new NovoVeiculo();
        novoveiculo.setRenavamNovoVeiculo("44745162039");
        novoveiculo.setPlacaNovoVeiculo("HWJ6E63");
        novoveiculo.setUfPlacaNovoVeiculo("CE");
        novoveiculo.setCorNovoVeiculo("Amarelo");
        novoveiculo.setAnoFabricacaoNovoVeiculo("2022");
        novoveiculo.setAnoModeloNovoVeiculo("2023");
        novoveiculo.setChassiNovoVeiculo("76482157359875462");
    }

    @Test
    void DeveRetornarStatusCodeCreated_AoCadastrarNovoVeiculo(){
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        Mockito.when(repository.save(veiculo)).thenReturn(veiculo);
        Mockito.when(repository.findById("44745162039")).thenReturn(Optional.empty());

        ResponseEntity response = service.cadastrarNovoVeiculo(novoveiculo,uriBuilder.path("/cadastraveiculo").build(novoveiculo));

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(Veiculo.class));
    }

    @Test
    void DeveRetornarStatusCodeOKComErro_AoCadastrarNovoVeiculo(){
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

        // cadastro de veiculo já existente
        Mockito.when(repository.findById("44745162039")).thenReturn(Optional.of(veiculo));

        ResponseEntity response = service.cadastrarNovoVeiculo(novoveiculo,uriBuilder.path("/cadastraveiculo").build(novoveiculo));

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Cadastro não realizado!",response.getBody());
        Mockito.verify(repository, Mockito.times(1)).findById(ArgumentMatchers.anyString());
    }


}
