package com.api.veiculo.controller;

import com.api.veiculo.model.Veiculo;
import com.api.veiculo.dto.VeiculoResponse;
import com.api.veiculo.mapper.VeiculoMapper;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import jakarta.validation.Valid;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/veiculos")
@Tag(name = "Veículos", description = "API para o gerenciamento de Veículos")
public class VeiculoController {

    private final com.api.veiculo.service.VeiculoService service;
    private final VeiculoMapper mapper;

    public VeiculoController(com.api.veiculo.service.VeiculoService service, VeiculoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "Lista todos os veículos", description = "Retorna uma lista com todos os veículos cadastrados no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de veículos recuperada com sucesso")
    @GetMapping
    public ResponseEntity<List<VeiculoResponse>> getAllVeiculos() {
        List<VeiculoResponse> response = service.findAll().stream()
                .map(mapper::toVeiculoResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Busca veículo por Renavam", description = "Retorna os detalhes de um veículo específico a partir do seu Renavam.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo encontrado"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    })
    @GetMapping("/{renavam}")
    public ResponseEntity<VeiculoResponse> getVeiculoByRenavam(@PathVariable String renavam) {
        Veiculo veiculo = service.buscaVeiculoComRenavam(renavam);
        return veiculo != null ? ResponseEntity.ok(mapper.toVeiculoResponse(veiculo)) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Cadastra um novo veículo", description = "Salva um novo veículo. Valida regras de negócio (Renavam/Placa únicas e UF existente).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Veículo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex: Renavam já cadastrado, UF inválida)"),
    })
    @PostMapping("/cadastraveiculo")
    public ResponseEntity<Void> cadastrarVeiculo(@RequestBody @Valid com.api.veiculo.dto.NovoVeiculo novoVeiculo, UriComponentsBuilder uriBuilder) {
        service.cadastrarNovoVeiculo(novoVeiculo);
        var uri = uriBuilder.path("/veiculos/{renavam}").buildAndExpand(novoVeiculo.renavamNovoVeiculo()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Atualiza os dados de um veículo", description = "Altera um veículo existente usando o Renavam atual como chave.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Veículo atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou Veículo não encontrado")
    })
    @PutMapping("/alteraveiculo")
    public ResponseEntity<Void> alterarVeiculo(@RequestBody @Valid com.api.veiculo.dto.NovoVeiculo veiculo) {
        service.alterarDadosVeiculo(veiculo);
        return ResponseEntity.noContent().build(); // 204 No Content para atualizações bem-sucedidas
    }

    @Operation(summary = "Deleta um veículo do sistema", description = "Remove permanentemente um veículo através do seu número de Renavam.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Veículo deletado com sucesso (ou já inexistente)"),
    })
    @DeleteMapping("/deletaveiculo/{renavam}")
    public ResponseEntity<Void> deletarVeiculo(@PathVariable String renavam) {
        service.deletarVeiculo(renavam);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Busca veículo pela placa completa", description = "Retorna um único veículo correspondente à placa exata informada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo encontrado"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    })
    @GetMapping("/buscaplaca/{placa}")
    public ResponseEntity<VeiculoResponse> buscaPorPlaca(@PathVariable String placa) {
        Veiculo veiculo = service.buscaVeiculoComPlaca(placa);
        return veiculo != null ? ResponseEntity.ok(mapper.toVeiculoResponse(veiculo)) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Busca veículos por UF da Placa", description = "Retorna a lista de veículos cuja placa pertença ao estado (UF) informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping("/placa/{uf}")
    public ResponseEntity<List<VeiculoResponse>> buscaPorUf(@PathVariable String uf) {
        List<VeiculoResponse> response = service.buscaVeiculoComUfDaPlaca(uf).stream()
                .map(mapper::toVeiculoResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Busca veículos por intervalo de aquisição", description = "Busca veículos adquiridos dentro do período especificado (formato dd-MM-yyyy).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Formato de data inválido")
    })
    @GetMapping("/intervaloaquisicao/{datainicio}/{datafim}")
    public ResponseEntity<List<VeiculoResponse>> buscaPorIntervaloAquisicao(
            @PathVariable String datainicio, 
            @PathVariable String datafim) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate start = LocalDate.parse(datainicio, formatter);
        LocalDate end = LocalDate.parse(datafim, formatter);
        List<VeiculoResponse> response = service.buscaVeiculosComIntervaloAquisicao(start, end).stream()
                .map(mapper::toVeiculoResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
