package com.trix.crud.controller;

import com.trix.crud.dto.NovoVeiculo;
import com.trix.crud.modelo.Veiculo;
import com.trix.crud.service.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {
    
    @Autowired
    VeiculoService service;

    @PostMapping("/cadastraveiculo")
    public void cadastraNovoVeiculo(@RequestBody NovoVeiculo novoVeiculo){
        service.cadastrarNovoVeiculo(novoVeiculo);
    }

    @GetMapping
    public ResponseEntity<List<Veiculo>> listaVeiculos(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{renavam}")
    public ResponseEntity buscaveiculo(@PathVariable String renavam){
        return service.buscaVeiculoComRenavam(renavam);
    }

    @PutMapping("/alteraveiculo")
    public void alterarveiculo(@RequestBody Veiculo veiculo){
        service.alterarDadosVeiculo(veiculo);
    }

    @DeleteMapping("/deletaveiculo")
    public void deletarveiculo(@RequestBody String renavam){
        service.deletarVeiculo(renavam);
    }


    @GetMapping("/placa/{uf}")
    public ResponseEntity buscaufplaca(@PathVariable String uf){
        return service.buscaVeiculoComUfDaPlaca(uf);
    }
    
    @GetMapping("/buscaplaca/{placa}")
    public ResponseEntity buscaplaca(@PathVariable String placa) {
        return service.buscaVeiculoComPlaca(placa);
    }

    @GetMapping("/intervaloaquisicao/{datainicio}/{datafim}")
    public List<Veiculo> intervalodataaquisicao(@PathVariable("datainicio") String datainicio,
                                                @PathVariable("datafim") String datafim){
        return null;//service.buscaVeiculosComIntervaloAquisicao(datainicio, datafim).getBody();
    }
}
