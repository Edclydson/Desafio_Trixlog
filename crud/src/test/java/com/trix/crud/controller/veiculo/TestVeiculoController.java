package com.trix.crud.controller.veiculo;

import com.trix.crud.ApplicationConfigTest;
import com.trix.crud.controller.VeiculoController;
import com.trix.crud.dto.NovoVeiculo;
import com.trix.crud.modelo.Veiculo;
import com.trix.crud.service.VeiculoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class TestVeiculoController extends ApplicationConfigTest {

    @MockBean
    VeiculoService service;

    @Autowired
    VeiculoController controller;

    private Veiculo veiculo;

    private NovoVeiculo novoveiculo;

    private List<Veiculo> lista = new ArrayList<>();

    private Optional<Veiculo> veiculoOptional;

    private String dataInicio;

    private String dataFim;

    @BeforeEach
    void setup(){
        veiculo = Mockito.mock(Veiculo.class);
        Mockito.when(veiculo.getRenavam()).thenReturn("84162312041");
        Mockito.when(veiculo.getCnhCondutor()).thenReturn("");
        Mockito.when(veiculo.getNomeCondutor()).thenReturn("");
        Mockito.when(veiculo.getAnoFabricacao()).thenReturn("2013");
        Mockito.when(veiculo.getAnoModelo()).thenReturn("2014");
        Mockito.when(veiculo.getPlaca()).thenReturn("HYA5489");
        Mockito.when(veiculo.getUfPlaca()).thenReturn("CE");
        Mockito.when(veiculo.getCor()).thenReturn("Bege");
        Mockito.when(veiculo.getChassi()).thenReturn("2tJ0r3ZVlVH0C8821".toUpperCase());
        Mockito.when(veiculo.getDataAquisicao()).thenReturn("2021-12-11");

        novoveiculo = Mockito.mock(NovoVeiculo.class);
        Mockito.when(novoveiculo.getChassiNovoVeiculo()).thenReturn("8ATK9HPL8zA314828".toUpperCase());
        Mockito.when(novoveiculo.getCnhCondutorNovoVeiculo()).thenReturn("");
        Mockito.when(novoveiculo.getNomeCondutorNovoVeiculo()).thenReturn("");
        Mockito.when(novoveiculo.getRenavamNovoVeiculo()).thenReturn("44745162039");
        Mockito.when(novoveiculo.getCorNovoVeiculo()).thenReturn("Amarelo");
        Mockito.when(novoveiculo.getAnoFabricacaoNovoVeiculo()).thenReturn("1999");
        Mockito.when(novoveiculo.getAnoModeloNovoVeiculo()).thenReturn("2000");
        Mockito.when(novoveiculo.getDataAquisicaoNovoVeiculo()).thenReturn("");
        Mockito.when(novoveiculo.getPlacaNovoVeiculo()).thenReturn("HWJ6563");
        Mockito.when(novoveiculo.getUfPlacaNovoVeiculo()).thenReturn("CE");

        veiculoOptional = Mockito.mock(Optional.class);
        Mockito.when(veiculoOptional.get()).thenReturn(veiculo);

        dataInicio = "2022-01-01";
        dataFim = "2022-08-01";

    }

    @Test
    void DeveListarTodosOsVeiculos(){
        lista.add(veiculo);
        Mockito.when(service.findAll()).thenReturn(lista);

        assertNotNull(controller.listaVeiculos());
        assertEquals(lista,controller.listaVeiculos());
        assertEquals(veiculo,controller.listaVeiculos().get(0));
    }

    @Test
    void DeveBuscarUmVeiculo(){
        Mockito.when(service.buscaveiculo(veiculo.getRenavam())).thenReturn(veiculoOptional);

        assertNotNull(controller.buscaveiculo(veiculo.getRenavam()));
        assertEquals(veiculo,controller.buscaveiculo(veiculo.getRenavam()).get());
        assertEquals(veiculoOptional,controller.buscaveiculo(veiculo.getRenavam()));
    }

    @Test
    void DeveBuscarVeiculoPelaUfDaPlaca(){
        lista.add(veiculo);
        Mockito.when(service.buscaveiculoufplaca(veiculo.getUfPlaca())).thenReturn(lista);

        assertEquals(lista,controller.buscaufplaca(veiculo.getUfPlaca()));
        assertNotNull(controller.buscaufplaca("CE"));
    }

    @Test
    void DeveBuscarVeiculoPelaPlaca(){
        lista.add(veiculo);
        Mockito.when(service.buscaplaca(veiculo.getPlaca())).thenReturn(lista);
        controller.buscaplaca(veiculo.getPlaca());
        Mockito.verify(service,Mockito.times(1)).buscaplaca(ArgumentMatchers.anyString());
    }

    @Test
    void DeveBuscarIntervaloDeAquisicao(){
        veiculo.setDataAquisicao(LocalDate.now().toString());
        lista.add(veiculo);
        Mockito.when(service.intervaloaquisicao(dataInicio,dataFim)).thenReturn(lista);

        assertNotNull(controller.intervalodataaquisicao(dataInicio,dataFim));
        assertEquals(lista,controller.intervalodataaquisicao(dataInicio,dataFim));
    }

    @Test
    void DeveCadastrarUmNovoVeiculo(){
        controller.cadastraNovoVeiculo(novoveiculo);
        assertNotNull(novoveiculo);
        Mockito.verify(service, Mockito.times(1)).registraNovoVeiculo(ArgumentMatchers.any(NovoVeiculo.class));
    }

    @Test
    void DeveAlterarUmVeiculo(){
        veiculo.setPlaca("HYA5499");
        controller.alterarveiculo(veiculo);
        assertNotNull(veiculo);
        Mockito.verify(service, Mockito.times(1)).alteraveiculo(ArgumentMatchers.any(Veiculo.class));
    }

    @Test
    void DeveDeletarVeiculo(){
        controller.deletarveiculo(veiculo.getRenavam());
        Mockito.verify(service,Mockito.times(1)).deletaveiculo(ArgumentMatchers.anyString());
    }
}
