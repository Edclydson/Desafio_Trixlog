package com.trix.crud.controller;

import com.trix.crud.dto.NovoVeiculo;
import com.trix.crud.modelo.Veiculo;
import com.trix.crud.service.VeiculoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
@Api(tags = {"Controlador de Veículos"})
public class VeiculoController {
    

    private final VeiculoService service;

    public VeiculoController(VeiculoService service) {
        this.service = service;
    }

    @PostMapping("/cadastraveiculo")
    @ApiOperation(value = "Realiza o registro de um novo veículo")
    public ResponseEntity cadastraNovoVeiculo(@RequestBody NovoVeiculo novoVeiculo, UriComponentsBuilder uriBuilder){
        var uri = uriBuilder.path("/cadastraveiculo").build(novoVeiculo);
        return service.cadastrarNovoVeiculo(novoVeiculo,uri);
    }

    @GetMapping
    @ApiOperation(value = "Realiza a listagem de todos os veículos cadastrados")
    public ResponseEntity<List<Veiculo>> listaVeiculos(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{renavam}")
    @ApiOperation(value = "Realiza a busca de um único veículo")
    public ResponseEntity buscaveiculo(@PathVariable("renavam") String renavam){
        return service.buscaVeiculoComRenavam(renavam);
    }

    @PutMapping("/alteraveiculo")
    @ApiOperation(value = "Realiza a alteração no registro de um veículo")
    public ResponseEntity alterarveiculo(@RequestBody NovoVeiculo veiculo){
        return service.alterarDadosVeiculo(veiculo);
    }

    @DeleteMapping("/deletaveiculo/{renavam}")
    @ApiOperation(value = "Realiza a exclusão de um veículo")
    public ResponseEntity deletarveiculo(@PathVariable("renavam") String renavam){
       return service.deletarVeiculo(renavam);
    }


    @GetMapping("/placa/{uf}")
    @ApiOperation(value = "Realiza a busca de veículos pela UF da placa")
    public ResponseEntity buscaufplaca(@PathVariable String uf){
        return service.buscaVeiculoComUfDaPlaca(uf);
    }
    
    @GetMapping("/buscaplaca/{placa}")
    @ApiOperation(value = "Realiza a busca de um veiculo pela placa")
    public ResponseEntity buscaplaca(@PathVariable String placa) {
        return service.buscaVeiculoComPlaca(placa);
    }

    @GetMapping("/intervaloaquisicao/{datainicio}/{datafim}")
    @ApiOperation(value = "Realiza a busca de veículos pelo intervalo de data da aquisição")
    public ResponseEntity intervalodataaquisicao(@PathVariable("datainicio") String datainicio,
                                                @PathVariable("datafim") String datafim){
        return service.buscaVeiculosComIntervaloAquisicao(datainicio, datafim);
    }
}
