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
        service.registraNovoVeiculo(novoVeiculo);
    }

    @GetMapping
    public ResponseEntity<List<Veiculo>> listaVeiculos(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{renavam}")
    public ResponseEntity buscaveiculo(@PathVariable String renavam){
        return service.buscaveiculo(renavam);
    }

    @PutMapping("/alteraveiculo")
    public void alterarveiculo(@RequestBody Veiculo veiculo){
        service.alteraveiculo(veiculo);
    }

    @DeleteMapping("/deletaveiculo")
    public void deletarveiculo(@RequestBody String renavam){
        service.deletaveiculo(renavam);
    }


    @GetMapping("/placa/{uf}")
    public ResponseEntity buscaufplaca(@PathVariable String uf){
        return service.buscaveiculoufplaca(uf);
    }
    
    @GetMapping("/veiculo/{placa}")
    public List<Veiculo> buscaplaca(@PathVariable String placa){
        return service.buscaplaca(placa);
    }

    @GetMapping("/intervaloaquisicao/{datainicio}/{datafim}")
    public List<Veiculo> intervalodataaquisicao(@PathVariable String datainicio, @PathVariable String datafim){
        return service.intervaloaquisicao(datainicio, datafim);
    }
}
