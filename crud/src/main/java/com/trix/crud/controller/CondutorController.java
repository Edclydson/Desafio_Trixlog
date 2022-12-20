package com.trix.crud.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/condutores")
public class CondutorController {

    @Autowired
    CondutorService service;

    @PostMapping("/cadastrocondutor")
    public ResponseEntity cadastrar(@RequestBody NovoCondutor novoCondutor, UriComponentsBuilder uriBuilder){
        if(service.cadastraNovoCondutor(novoCondutor)){
            var uri = uriBuilder.path("/condutores/cadastrocondutor").build(novoCondutor);
            return ResponseEntity.created(uri).body("Condutor cadastrado com sucesso!");
        }
        return ResponseEntity.ok("Cadastro não realizado! Verifique os campos e tente novamente.");
    }

    @GetMapping
    public ResponseEntity<List<Condutor>> listaCondutores(){
        return ResponseEntity.ok(service.consultaTodosCondutores());
    }

    @GetMapping("/buscacondutor/{cnh}")
    public ResponseEntity buscaCondutor(@PathVariable String cnh){
        return service.consultaCondutorcnh(cnh);
    }

    @PutMapping("/alteracondutor")
    public ResponseEntity alterar(@RequestBody Condutor condutor){
        return service.alteraCondutor(condutor);
    }

    @DeleteMapping("/deletacondutor/{cnh}")
    public ResponseEntity deletar(@PathVariable String cnh){
        return service.deletaCondutor(cnh);
    }

    @GetMapping("/condutor/{nome_condutor}")
    public ResponseEntity buscanomeCondutor(@PathVariable("nome_condutor") String nomeCondutor){
        return service.buscaNomeCondutor(nomeCondutor);
    }
}
