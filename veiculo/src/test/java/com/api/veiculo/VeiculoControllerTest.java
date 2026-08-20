package com.api.veiculo;

import com.api.veiculo.controller.VeiculoController;
import com.api.veiculo.dto.NovoVeiculo;
import com.api.veiculo.model.Veiculo;
import com.api.veiculo.service.VeiculoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VeiculoController.class)
class VeiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VeiculoService veiculoService;

    @MockitoBean
    private com.api.veiculo.mapper.VeiculoMapper veiculoMapper;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private NovoVeiculo novoVeiculoValido;
    private Veiculo veiculoValido;

    @BeforeEach
    void setUp() {
        novoVeiculoValido = new NovoVeiculo(
                "12345678901", "ABC1234", "9BWZZZ37ZKT000000",
                "2023", "2023", "Prata", "SP", LocalDate.now(), null
        );

        veiculoValido = new Veiculo();
        veiculoValido.setRenavam("12345678901");
        veiculoValido.setPlaca("ABC1234");
        veiculoValido.setCor("Prata");
    }

    @Test
    void deveCadastrarVeiculoComSucesso() throws Exception {
        doNothing().when(veiculoService).cadastrarNovoVeiculo(any(NovoVeiculo.class));

        mockMvc.perform(post("/veiculos/cadastraveiculo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoVeiculoValido)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/veiculos/12345678901"));

        verify(veiculoService, times(1)).cadastrarNovoVeiculo(any(NovoVeiculo.class));
    }

    @Test
    void deveBuscarVeiculoPorRenavamComSucesso() throws Exception {
        when(veiculoService.buscaVeiculoComRenavam("12345678901")).thenReturn(veiculoValido);
        when(veiculoMapper.toVeiculoResponse(any(Veiculo.class))).thenReturn(
                new com.api.veiculo.dto.VeiculoResponse(UUID.randomUUID(), "12345678901", "ABC1234", null, null, null, null, null, null, null)
        );

        mockMvc.perform(get("/veiculos/12345678901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.renavam").value("12345678901"))
                .andExpect(jsonPath("$.placa").value("ABC1234"));
    }

    @Test
    void deveRetornarNotFoundAoBuscarRenavamInexistente() throws Exception {
        when(veiculoService.buscaVeiculoComRenavam("00000000000")).thenReturn(null);

        mockMvc.perform(get("/veiculos/00000000000"))
                .andExpect(status().isNotFound());
    }
}
