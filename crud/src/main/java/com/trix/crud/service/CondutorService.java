package com.trix.crud.service;

import com.trix.crud.dto.NovoCondutor;
import com.trix.crud.modelo.Condutor;
import com.trix.crud.modelo.Veiculo;
import com.trix.crud.repository.CondutorRepository;
import com.trix.crud.repository.VeiculoRepository;
import com.trix.crud.service.interfaces.CondutorInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CondutorService implements CondutorInterface{


    private final CondutorRepository repository;

    private final VeiculoRepository veiculoRepository;

    private final VeiculoService veiculoService;

    private final CondutorValidacoes valida;

    private final CondutorAcoes acao;

    public CondutorService(CondutorRepository repository,
                           VeiculoRepository veiculoRepository,
                           VeiculoService veiculoService, CondutorValidacoes valida, CondutorAcoes acao) {
        this.repository = repository;
        this.veiculoRepository = veiculoRepository;
        this.veiculoService = veiculoService;
        this.valida = valida;
        this.acao = acao;
    }


    @Override
    public boolean cadastraNovoCondutor(NovoCondutor novoCondutor){
        if(valida.cnhValida(novoCondutor.getNumCnh()) && valida.nomeCondutor(novoCondutor.getNome())){
            repository.save(acao.geraCondutor(novoCondutor));
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List<Condutor> consultaTodosCondutores(){
            List<Condutor> todosCondutores = repository.findAll();
            return todosCondutores.isEmpty() ? Collections.emptyList() : todosCondutores;
    }

    @Override
    public ResponseEntity consultaCondutorcnh(String cnh){
        if(valida.cnhValida(cnh)){
            if(repository.findById(cnh).isPresent())
                return ResponseEntity.ok(repository.findById(cnh).get());
            else{
                return ResponseEntity.ok("Condutor não encontrado!");
            }
        }
            return ResponseEntity.ok("CNH informada não é válida!");
    }

    @Override
    public ResponseEntity alteraCondutor(Condutor condutor){
       if(valida.nomeCondutor(condutor.getNomeCondutor())){
           repository.save(acao.alteracaoCondutor(condutor));
           return ResponseEntity.ok("Alterações salvas com sucesso!");
       }
       return ResponseEntity.ok("Houve um problema ao salvar as alterações");
    }

    @Override
    public ResponseEntity deletaCondutor(String cnh){
        if(valida.cnhValida(cnh) && repository.findById(cnh).isPresent()){
            repository.deleteById(cnh);
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok("Houve um problema na hora de remover o condutor");
        }

    }

    @Override
    public ResponseEntity buscaNomeCondutor(String nomeCondutor){
        if(valida.nomeCondutor(nomeCondutor)){
            if(nomeCondutor.contains(" ")){
                return ResponseEntity.ok(repository.findByNomeCondutor(nomeCondutor));
            }
            return ResponseEntity.ok(repository.findByNomeCondutorContaining(nomeCondutor));
        }
        return ResponseEntity.ok("Nome inválido!");
    }


    @Override //PRECISA MELHORAR
    public ResponseEntity adquirirVeiculo(String renavam, String cnh){
        if(veiculoService.verificacaoVeiculoParaAquisicao(renavam) && valida.requisitosAquisicaoVeiculo(cnh)){
            Condutor condutorComVeiculo = repository.findById(cnh).get();
            List<Veiculo> novaLista = new ArrayList<>(condutorComVeiculo.getListaDeVeiculos());
            novaLista.add(veiculoRepository.findById(renavam).get());
            condutorComVeiculo.setListaDeVeiculos(novaLista);
            repository.save(condutorComVeiculo);
            veiculoService.atribuirCondutorAoVeiculo(renavam, cnh);
            return ResponseEntity.ok("Veículo adquirido com sucesso");
        }
        return ResponseEntity.ok("Para adquerir um veiculo informe os dados corretamente.");
    }

    @Override  //PRECISA MELHORAR
    public ResponseEntity liberarVeiculo(String renavam, String cnh) {
        if (valida.temAlgumVeiculo(cnh) && valida.possuiOVeiculo(renavam, cnh)
                && valida.cnhValida(cnh)){
            Condutor condutor = repository.findById(cnh).get();
            List<Veiculo> novaLista = new ArrayList<>(condutor.getListaDeVeiculos());
            novaLista.remove(veiculoService.LiberacaoVeiculo(renavam));
            condutor.setListaDeVeiculos(novaLista);
            repository.save(condutor);
            return ResponseEntity.ok("O Condutor não tem mais posse do veículo: " + renavam);
        }
        return ResponseEntity.ok("Requisição não foi processada! Tente novamente.");
    }
}
