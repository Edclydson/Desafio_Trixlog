package com.trix.crud.veiculo;

import com.trix.crud.ApplicationConfigTest;
import com.trix.crud.controller.VeiculoController;
import com.trix.crud.dto.NovoVeiculo;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

class TestVeiculoController extends ApplicationConfigTest{


    @Autowired
    VeiculoController controller;

    @MockBean
    VeiculoService service;

    private NovoVeiculo novoveiculo;

    ResponseEntity response;

    @BeforeEach
    void setup(){
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
    void DeveRetornarStatusCodeCreatedComSucesso_AocadastraNovoVeiculo() {
        var uriDoService = UriComponentsBuilder.newInstance().path("/cadastraveiculo").build(novoveiculo);
        var uriDoController = UriComponentsBuilder.newInstance();
        var uriRetorno = UriComponentsBuilder.newInstance().path("/cadastraveiculo").build();

        Mockito.when(service.cadastrarNovoVeiculo(novoveiculo,uriDoService)).thenReturn(ResponseEntity.created(uriRetorno.toUri()).build());
        response = controller.cadastraNovoVeiculo(novoveiculo,uriDoController);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
    }
    @Test
    void DeveRetornarStatusCodeOKComErro_AocadastraNovoVeiculo() {
        var uriDoService = UriComponentsBuilder.newInstance().path("/cadastraveiculo").build(novoveiculo);
        var uriDoController = UriComponentsBuilder.newInstance();

        Mockito.when(service.cadastrarNovoVeiculo(novoveiculo,uriDoService)).thenReturn(ResponseEntity.ok().build());
        response = controller.cadastraNovoVeiculo(novoveiculo,uriDoController);

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void DeveRetornarStatusCodeOK_Aobuscaveiculo() {
        Mockito.when(service.buscaVeiculoComRenavam("44745162039")).thenReturn(ResponseEntity.ok().build());

        response = controller.buscaveiculo("44745162039");

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void DeveRetornarStatusCodeOK_Aoalterarveiculo() {
        Mockito.when(service.alterarDadosVeiculo(novoveiculo)).thenReturn(ResponseEntity.ok().build());

        response = controller.alterarveiculo(novoveiculo);

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void DeveRetornarStatusCodeNoContent_Aodeletarveiculo() {
        Mockito.when(service.deletarVeiculo("44745162039")).thenReturn(ResponseEntity.noContent().build());

        response = controller.deletarveiculo("44745162039");

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());

        Mockito.verify(service).deletarVeiculo(ArgumentMatchers.anyString());
    }

    @Test
    void DeveRetornarStatusCodeNotFound_Aodeletarveiculo() {
        Mockito.when(service.deletarVeiculo("44745162039")).thenReturn(ResponseEntity.notFound().build());

        response = controller.deletarveiculo("44745162039");

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());

        Mockito.verify(service).deletarVeiculo(ArgumentMatchers.anyString());
    }

    @Test
    void DeveRetornarStatusCodeOK_Aodeletarveiculo() {
        Mockito.when(service.deletarVeiculo("44745162039")).thenReturn(ResponseEntity.ok().build());

        response = controller.deletarveiculo("44745162039");

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());

        Mockito.verify(service).deletarVeiculo(ArgumentMatchers.anyString());
    }

    @Test
    void DeveRetornarStatusCodeOK_Aobuscaufplaca() {
        Mockito.when(service.buscaVeiculoComUfDaPlaca("CE")).thenReturn(ResponseEntity.ok().build());

        response = controller.buscaufplaca("CE");
        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());

        Mockito.verify(service).buscaVeiculoComUfDaPlaca(ArgumentMatchers.anyString());
    }

    @Test
    void DeveRetornarStatusCodeOK_Aobuscaplaca() {
        Mockito.when(service.buscaVeiculoComPlaca("HWJ6E63")).thenReturn(ResponseEntity.ok().build());

        response = controller.buscaplaca("HWJ6E63");
        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());

        Mockito.verify(service).buscaVeiculoComPlaca(ArgumentMatchers.anyString());
    }

    @Test
    void DeveRetornarStatusCodeOK_Aointervalodataaquisicao() {
        Mockito.when(service.buscaVeiculosComIntervaloAquisicao("01-01-2023","20-01-2023"))
                .thenReturn(ResponseEntity.ok().build());

        response = controller.intervalodataaquisicao("01-01-2023","20-01-2023");
        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());

        verify(service).buscaVeiculosComIntervaloAquisicao(ArgumentMatchers.anyString(),ArgumentMatchers.anyString());
    }
}