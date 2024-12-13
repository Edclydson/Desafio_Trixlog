package com.trix.crud.service;

import com.trix.crud.dto.NewDriver;
import com.trix.crud.modelo.Condutor;
import com.trix.crud.repository.CondutorRepository;
import com.trix.crud.service.interfaces.condutor.CondutorInterface;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CondutorService implements CondutorInterface{


    private final CondutorRepository repository;

    private final CondutorValidacoes valida;

    private final CondutorAcoes acao;

    private final VeiculoValidacoes veiculoValidacoes;

    private final VeiculoAcoes veiculoAcoes;


    public CondutorService(CondutorRepository repository,
                           CondutorValidacoes valida, VeiculoValidacoes veiculoValidacoes, VeiculoAcoes veiculoAcoes, CondutorAcoes acao) {
        this.repository = repository;
        this.valida = valida;
        this.veiculoValidacoes = veiculoValidacoes;
        this.veiculoAcoes = veiculoAcoes;
        this.acao = acao;
    }


    @Override
    public boolean cadastraNovoCondutor(NewDriver newDriver){
        try{
            if(valida.cnhValida(newDriver.cnhNumber()) && valida.nomeCondutor(newDriver.nameDriver())){
                repository.save(acao.geraCondutor(newDriver));
                return true;
            }
        }
        catch(ValidationException e){
            throw new ValidationException("Erro de validação!");
        }
        catch(PersistenceException e){
            throw new PersistenceException("Erro ao cadastrar novo condutor!");
        }
        return false;
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
       if(valida.nomeCondutor(condutor.getNomeCondutor()) && valida.existe(condutor.getNumeroCnh())){
           repository.save(acao.alteracaoCondutor(condutor));
           return ResponseEntity.ok("Alterações salvas com sucesso!");
       }
       return ResponseEntity.ok("Houve um problema ao salvar as alterações");
    }

    @Override
    public ResponseEntity deletaCondutor(String cnh){
        if(valida.cnhValida(cnh) && valida.existe(cnh)){
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
        if(veiculoValidacoes.requisitosAquisicaoVeiculo(renavam) && valida.requisitosAquisicaoVeiculo(cnh)){
            repository.save(acao.acquireProcess(cnh,renavam));
            veiculoAcoes.atribuirCondutorAoVeiculo(renavam, cnh);
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
