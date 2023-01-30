package com.trix.crud.service;

import com.trix.crud.dto.NovoCondutor;
import com.trix.crud.modelo.Condutor;
import com.trix.crud.repository.CondutorRepository;
import com.trix.crud.repository.VeiculoRepository;
import com.trix.crud.service.interfaces.condutor.CondutorInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
            Optional<Condutor> condutorBuscado = repository.findById(cnh);
            return condutorBuscado.isPresent()? ResponseEntity.ok(condutorBuscado.get())
                    : ResponseEntity.ok("Condutor não encontrado!");
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
            return nomeCondutor.contains(" ") ? ResponseEntity.ok(repository.findByNomeCondutor(nomeCondutor))
                    : ResponseEntity.ok(repository.findByNomeCondutorContaining(nomeCondutor));
        }
        return ResponseEntity.ok("Nome inválido!");
    }


    @Override
    public ResponseEntity adquirirVeiculo(String renavam, String cnh){
        if(veiculoService.verificacaoVeiculoParaAquisicao(renavam) && valida.requisitosAquisicaoVeiculo(cnh)){
            repository.save(acao.acquireProcess(cnh,renavam));
            veiculoService.atribuirCondutorAoVeiculo(renavam, cnh);
            return ResponseEntity.ok("Veículo adquirido com sucesso");
        }
        return ResponseEntity.ok("Para adquerir um veiculo informe os dados corretamente.");
    }

    @Override
    public ResponseEntity liberarVeiculo(String renavam, String cnh) {
        if (valida.temAlgumVeiculo(cnh) && valida.possuiOVeiculo(renavam, cnh) && valida.cnhValida(cnh)){
            repository.save(acao.loseProcess(cnh,renavam));
            return ResponseEntity.ok("O Condutor não tem mais posse do veículo: " + renavam);
        }
        return ResponseEntity.ok("Requisição não foi processada! Tente novamente.");
    }
}
