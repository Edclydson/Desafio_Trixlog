package com.api.condutor.controller;

import com.api.condutor.dto.CondutorRequest;
import com.api.condutor.dto.CondutorResponse;
import com.api.condutor.service.CondutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/condutores")
@Tag(name = "Condutores", description = "Gerenciamento de motoristas e vínculos de frota")
public class CondutorController {

    private final CondutorService service;

    public CondutorController(CondutorService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastra um novo condutor", description = "Cria um novo registro de condutor. A CNH deve ser única e válida.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Condutor criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou CNH já cadastrada", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/cadastrocondutor")
    public ResponseEntity<CondutorResponse> cadastrar(@Valid @RequestBody CondutorRequest request, UriComponentsBuilder uriBuilder){
        CondutorResponse salvo = service.cadastraNovoCondutor(request);
        URI uri = uriBuilder.path("/condutores/buscacondutor/{cnh}").buildAndExpand(salvo.numeroCnh()).toUri();
        return ResponseEntity.created(uri).body(salvo);
    }

    @Operation(summary = "Lista todos os condutores", description = "Retorna uma lista paginada (atualmente geral) de todos os condutores cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<CondutorResponse>> listaCondutores(){
        return ResponseEntity.ok(service.consultaTodosCondutores());
    }

    @Operation(summary = "Busca condutor por CNH", description = "Retorna as informações do condutor utilizando o número da CNH.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Condutor encontrado"),
            @ApiResponse(responseCode = "404", description = "CNH não encontrada na base de dados", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/buscacondutor/{cnh}")
    public ResponseEntity<CondutorResponse> buscaCondutor(@PathVariable String cnh){
        return ResponseEntity.ok(service.consultaCondutorcnh(cnh));
    }

    @Operation(summary = "Altera os dados de um condutor", description = "Atualiza os dados cadastrais (ex. Nome) de um condutor já existente informando sua CNH.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Condutor atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Condutor não encontrado para alteração", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Falha de validação dos dados", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/alteracondutor")
    public ResponseEntity<CondutorResponse> alterar(@Valid @RequestBody CondutorRequest request){
        CondutorResponse alterado = service.alteraCondutor(request);
        return ResponseEntity.ok(alterado);
    }

    @Operation(summary = "Remove um condutor", description = "Deleta fisicamente um condutor a partir da sua CNH.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Condutor removido com sucesso (Sem conteúdo)"),
            @ApiResponse(responseCode = "404", description = "Condutor não encontrado para deleção", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/deletacondutor/{cnh}")
    public ResponseEntity<Void> deletar(@PathVariable String cnh){
        service.deletaCondutor(cnh);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Pesquisa condutores pelo Nome", description = "Realiza uma pesquisa (exata ou parcial) de condutores baseada no nome informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pesquisa realizada com sucesso")
    })
    @GetMapping("/condutor/{nome_condutor}")
    public ResponseEntity<List<CondutorResponse>> buscanomeCondutor(@PathVariable("nome_condutor") String nomeCondutor){
        return ResponseEntity.ok(service.buscaNomeCondutor(nomeCondutor));
    }

    @Operation(summary = "Associa um veículo a um condutor", description = "Adiciona a posse de um determinado veículo (RENAVAM) ao portfólio do condutor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo vinculado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Condutor não encontrado na base", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/addveiculo/{cnh}/{renavam}")
    public ResponseEntity<CondutorResponse> addVeiculo(@PathVariable("renavam") String renavam, @PathVariable("cnh") String cnh){
        CondutorResponse atualizado = service.adquirirVeiculo(renavam, cnh);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Libera a posse de um veículo", description = "Remove o veículo (RENAVAM) da lista de posse do condutor. Retorna erro se ele não possuir o veículo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo liberado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Condutor não possui o veículo para poder liberá-lo", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Condutor não encontrado na base", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/liberarveiculo/{cnh}/{renavam}")
    public ResponseEntity<CondutorResponse> liberarVeiculo(@PathVariable("renavam") String renavam, @PathVariable("cnh") String cnh){
        CondutorResponse atualizado = service.liberarVeiculo(renavam, cnh);
        return ResponseEntity.ok(atualizado);
    }
}
