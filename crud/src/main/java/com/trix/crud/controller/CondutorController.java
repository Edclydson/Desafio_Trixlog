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

import com.trix.crud.dto.NovoCondutor;
import com.trix.crud.modelo.Condutor;
import com.trix.crud.service.CondutorService;

@RestController
@RequestMapping("/condutores")
public class CondutorController {

    @Autowired
    CondutorService service;

    @PostMapping("/cadastrocondutor")
    public void cadastrar(@RequestBody NovoCondutor novoCondutor){
        service.cadastraNovoCondutor(novoCondutor);
    }

    @GetMapping
    public List<Condutor> listaCondutores(){
        return service.consultaTodosCondutores();
    }

    @GetMapping("/buscacondutor")
    public Optional<Condutor> buscaCondutor(String cnh){
        return service.consultaCondutorcnh(cnh);
    }

    @PutMapping("/alteracondutor")
    public void alterar(@RequestBody Condutor condutor){
        service.alteraCondutor(condutor);
    }

    @DeleteMapping("/deletacondutor")
    public void deletar(@RequestBody String cnh){
        service.deletaCondutor(cnh);
    }

    @GetMapping("/condutor/{nome_condutor}")
    public List<Condutor> buscanomeCondutor(@PathVariable String nomeCondutor){
        return service.buscaNomeCondutor(nomeCondutor);
    }
}
