package com.api.condutor.condutor;

import com.api.condutor.controller.CondutorController;
import com.api.condutor.dto.CondutorRequest;
import com.api.condutor.dto.CondutorResponse;
import com.api.condutor.service.CondutorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CondutorController.class)
public class TestCondutorController {

    @Autowired
    MockMvc mockMvc;
    
    @MockBean
    CondutorService service;
    
    @Autowired
    ObjectMapper mapper;

    private CondutorRequest request;
    private CondutorResponse response;

    @BeforeEach
    void setup(){
        request = new CondutorRequest("14867953214", "Zezin Danonão");
        response = new CondutorResponse("14867953214", "Zezin Danonão", new ArrayList<>());
    }

    @Test
    void DeveRetornarStatusCodeCreated_AoCadastrarNovoCondutor() throws Exception {
        Mockito.when(service.cadastraNovoCondutor(ArgumentMatchers.any(CondutorRequest.class))).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.post("/condutores/cadastrocondutor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void DeveRetornarBadRequest_AoFalharCadastro_Duplicado() throws Exception {
        Mockito.when(service.cadastraNovoCondutor(ArgumentMatchers.any(CondutorRequest.class))).thenThrow(new IllegalArgumentException("CNH duplicada"));
        mockMvc.perform(MockMvcRequestBuilders.post("/condutores/cadastrocondutor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void DeveRetornarBadRequest_AoFalharValidacao() throws Exception {
        // CNH Invalida
        CondutorRequest reqInvalido = new CondutorRequest("123", "Zezin");
        mockMvc.perform(MockMvcRequestBuilders.post("/condutores/cadastrocondutor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reqInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void DeveRetornarSucesso_QuandoListarUmCondutor() throws Exception {
        Mockito.when(service.consultaCondutorcnh(ArgumentMatchers.anyString())).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.get("/condutores/buscacondutor/14867953214"))
                .andExpect(status().isOk());
    }

    @Test
    void DeveRetornarNotFound_QuandoCondutorInexistente() throws Exception {
        Mockito.when(service.consultaCondutorcnh(ArgumentMatchers.anyString())).thenThrow(new EntityNotFoundException("Não achou"));
        mockMvc.perform(MockMvcRequestBuilders.get("/condutores/buscacondutor/14867953214"))
                .andExpect(status().isNotFound());
    }
}
