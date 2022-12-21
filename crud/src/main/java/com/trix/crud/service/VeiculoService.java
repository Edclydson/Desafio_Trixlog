package com.trix.crud.service;

import com.trix.crud.dto.NovoVeiculo;
import com.trix.crud.modelo.Condutor;
import com.trix.crud.modelo.Uf;
import com.trix.crud.modelo.Veiculo;
import com.trix.crud.repository.CondutorRepository;
import com.trix.crud.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class VeiculoService {

    @Autowired
    VeiculoRepository repository;

    @Autowired
    CondutorRepository condutorRepository;

    @Autowired
    CondutorService condutorS;

    public void registraNovoVeiculo(NovoVeiculo novoVeiculo){
//        if(buscaveiculo(novoVeiculo.getRenavamNovoVeiculo()).isPresent() == false){
//
//            Veiculo veiculo = new Veiculo();
//            veiculo.setRenavam(novoVeiculo.getRenavamNovoVeiculo());
//            veiculo.setChassi(novoVeiculo.getChassiNovoVeiculo().toUpperCase());
//            veiculo.setUfPlaca(novoVeiculo.getUfPlacaNovoVeiculo());
//            veiculo.setPlaca(novoVeiculo.getPlacaNovoVeiculo());
//            veiculo.setAnoFabricacao(novoVeiculo.getAnoFabricacaoNovoVeiculo());
//            veiculo.setAnoModelo(novoVeiculo.getAnoModeloNovoVeiculo());
//            veiculo.setDataAquisicao(novoVeiculo.getDataAquisicaoNovoVeiculo());
//            veiculo.setCor(novoVeiculo.getCorNovoVeiculo());
//
//            if(novoVeiculo.getCnhCondutorNovoVeiculo().equals("") && novoVeiculo.getNomeCondutorNovoVeiculo().equals("")){
//                veiculo.setCnhCondutor("");
//                veiculo.setNomeCondutor("");
//                repository.save(veiculo);
//            }
//            else{
//                veiculo.setCnhCondutor(novoVeiculo.getCnhCondutorNovoVeiculo());
//                veiculo.setNomeCondutor(novoVeiculo.getNomeCondutorNovoVeiculo());
//                repository.save(veiculo);
//
//                Condutor condutorComVeiculo = condutorRepository.findById(novoVeiculo.getCnhCondutorNovoVeiculo()).get();
//                condutorComVeiculo.getListaDeVeiculos().add(veiculo);
//                condutorS.addVeiculoCondutor(condutorComVeiculo);
//            }
//        }
    }    

    public List<Veiculo> findAll(){
        try{
            return (List<Veiculo>) repository.findAll();
        }catch (ClassCastException e){e.printStackTrace();}
        return Collections.emptyList();
    }

    public ResponseEntity buscaveiculo(String renavam){
        if(validaRenavam(renavam)){
            if (repository.findById(renavam).isPresent()){
                return ResponseEntity.ok(repository.findById(renavam));
            } else {
                return ResponseEntity.ok("Veículo não encontrado!");
            }
        }
        return ResponseEntity.ok("O renavam " + renavam + " não é válido!");
    }

    public void alteraveiculo(Veiculo veiculo){        
            Veiculo existente = repository.findById(veiculo.getRenavam()).get();
            String condutorApagar = existente.getCnhCondutor();
            existente.setPlaca(veiculo.getPlaca());
            existente.setChassi(veiculo.getChassi());
            existente.setAnoModelo(veiculo.getAnoModelo());
            existente.setAnoFabricacao(veiculo.getAnoFabricacao());
            existente.setCor(veiculo.getCor());
            existente.setUfPlaca(veiculo.getUfPlaca());
            existente.setDataAquisicao(veiculo.getDataAquisicao());
            existente.setNomeCondutor(veiculo.getNomeCondutor());
            existente.setCnhCondutor(veiculo.getCnhCondutor());
            existente.setRenavam(veiculo.getRenavam());

            if((existente.getCnhCondutor().equals("") && existente.getNomeCondutor().equals("")) ){
                Condutor condutorsemV = condutorRepository.findById(condutorApagar).get();
                condutorsemV.getListaDeVeiculos().remove(existente);
                condutorS.rmvVeiculoCondutor(condutorsemV);
            }else{
                Condutor condutorComVeiculo = condutorRepository.findById(veiculo.getCnhCondutor()).get();
                condutorComVeiculo.getListaDeVeiculos().add(veiculo);
                condutorS.addVeiculoCondutor(condutorComVeiculo);
            }
            
            
            repository.save(existente);
    }

    public void deletaveiculo(String renavam){
        repository.deleteById(renavam);
    }

    public ResponseEntity buscaveiculoufplaca(String uf){
        uf = uf.toUpperCase();
        if(validaUf(uf) && ufExistente(uf)){
            List<Veiculo> resposta = repository.findByufPlaca(uf);
            if(!resposta.isEmpty()){
                return ResponseEntity.ok(resposta);
            }
            else {return ResponseEntity.ok("Não encontramos nenhum veículo dessa localidade");}
        }
        return ResponseEntity.ok("Digite uma localidade válida");
    }

    public List<Veiculo> buscaplaca(String placa){
        if(placa.length() < 7){
            return repository.findByPlacaContaining(placa);
        }
        else{
            return repository.findByPlaca(placa);
        }
    }

    public List<Veiculo> intervaloaquisicao(String datainicio, String datafim){
        return repository.findByintevalo(datainicio, datafim);
    }


    private boolean validaRenavam(String renavam){
        if(renavam.matches("(?=.*\\d).{11}") && !renavam.matches("(?=.*[a-zA-Z]).+")){
            return true;
        }
        return false;
    }

    private boolean validaUf(String uf){
        return !uf.matches("(?=.*\\d).+") && uf.matches("(?=.*[a-zA-Z]).{2}");
    }

    private boolean ufExistente(String uf){
        for(Uf existente: Uf.values()){
            if(existente.toString().equals(uf)){
                return true;
            }
        }
        return false;
    }
}
