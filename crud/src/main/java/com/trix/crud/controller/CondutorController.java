package com.trix.crud.controller;


import com.trix.crud.dto.NovoCondutor;
import com.trix.crud.modelo.Condutor;
import com.trix.crud.service.CondutorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/condutores")
@Api(tags = {"Controlador de Condutores"})
public class CondutorController {

    private final CondutorService service;

    public CondutorController(CondutorService service) {
        this.service = service;
    }

    @PostMapping("/cadastrocondutor")
    @ApiOperation(value = "Realiza o registro de um novo condutor")
    public ResponseEntity cadastrar(@RequestBody NovoCondutor novoCondutor, UriComponentsBuilder uriBuilder){
        if(service.cadastraNovoCondutor(novoCondutor)){
            var uri = uriBuilder.path("/condutores/cadastrocondutor").build(novoCondutor);
            return ResponseEntity.created(uri).body("Condutor cadastrado com sucesso!");
        }
        return ResponseEntity.ok("Cadastro não realizado! Verifique os campos e tente novamente.");
    }

    @GetMapping
    @ApiOperation(value = "Realiza a listagem de todos os condutores registrados")
    public ResponseEntity<List<Condutor>> listaCondutores(){
        return ResponseEntity.ok(service.consultaTodosCondutores());
    }

    @GetMapping("/buscacondutor/{cnh}")
    @ApiOperation(value = "Realiza a busca de um condutor")
    public ResponseEntity buscaCondutor(@PathVariable String cnh){
        return service.consultaCondutorcnh(cnh);
    }

    @PutMapping("/alteracondutor")
    @ApiOperation(value = "Realiza a alteração no registro de um condutor")
    public ResponseEntity alterar(@RequestBody Condutor condutor){
        return service.alteraCondutor(condutor);
    }

    @DeleteMapping("/deletacondutor/{cnh}")
    @ApiOperation(value = "Realiza a exclusão de um registro de um condutor")
    public ResponseEntity deletar(@PathVariable String cnh){
        return service.deletaCondutor(cnh);
    }

    @GetMapping("/condutor/{nome_condutor}")
    @ApiOperation(value = "Realiza a busca de um condutor pelo nome")
    public ResponseEntity buscanomeCondutor(@PathVariable("nome_condutor") String nomeCondutor){
        return service.buscaNomeCondutor(nomeCondutor);
    }

    @PutMapping("/addveiculo/{cnh}/{renavam}")
    @ApiOperation(value = "Realiza a adição de um veiculo a lista de veiculos do condutor")
    public ResponseEntity addVeiculo(@PathVariable("renavam") String renavam, @PathVariable("cnh") String cnh){
        return service.adquirirVeiculo(renavam, cnh);
    }

    @PutMapping("/liberarveiculo/{cnh}/{renavam}")
    @ApiOperation(value = "Realiza a remoção de um veiculo da lista de veiculos do condutor")
    public ResponseEntity liberarVeiculo(@PathVariable("renavam") String renavam, @PathVariable("cnh") String cnh){
        return service.liberarVeiculo(renavam, cnh);
    }
}
