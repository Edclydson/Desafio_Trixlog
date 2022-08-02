package com.trix.crud.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trix.crud.dto.NovoVeiculo;
import com.trix.crud.modelo.Veiculo;
import com.trix.crud.service.VeiculoService;

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
    public List<Veiculo> listaVeiculos(){
        return service.findAll();
    }

    @GetMapping("/{renavam}")
    public Optional<Veiculo> buscaveiculo(String renavam){
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
    public List<Veiculo> buscaufplaca(@PathVariable String uf){
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
