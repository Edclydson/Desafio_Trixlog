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
public class VeiculoService implements iVeiculo{

    @Autowired
    VeiculoRepository repository;

    @Autowired
    CondutorRepository condutorRepository;

    @Autowired
    CondutorService condutorS;

    @Override
    public ResponseEntity cadastrarNovoVeiculo(NovoVeiculo novoVeiculo) {
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

        return null;
    }

    @Override
    public List<Veiculo> findAll() throws ClassCastException{
        try{
            return (List<Veiculo>) repository.findAll();
        }catch (ClassCastException e){e.printStackTrace();}
        return Collections.emptyList();
    }

    @Override
    public ResponseEntity buscaVeiculoComRenavam(String renavam) {
        if(validaRenavam(renavam)){
            if(repository.findById(renavam).isPresent()){
                return ResponseEntity.ok(repository.findById(renavam));
            } else {
                return ResponseEntity.ok("Veículo não encontrado!");
            }
        }
        return ResponseEntity.ok("O renavam " + renavam + " não é válido!");
    }

    @Override
    public ResponseEntity buscaVeiculoComUfDaPlaca(String ufDaPlaca) {
        ufDaPlaca = ufDaPlaca.toUpperCase();
        if(validaUf(ufDaPlaca) && ufExistente(ufDaPlaca)){
            List<Veiculo> resposta = repository.findByufPlaca(ufDaPlaca);
            if(!resposta.isEmpty()){
                return ResponseEntity.ok(resposta);
            }
            else {return ResponseEntity.ok("Não encontramos nenhum veículo dessa localidade");}
        }
        return ResponseEntity.ok("Digite uma localidade válida");
    }

    @Override
    public ResponseEntity buscaVeiculoComPlaca(String placa) {
        if(verificaTamanhoDaPlaca(placa)){
            return (validaPlaca(placa)) ? ResponseEntity.ok(repository.findByPlaca(placa)):
                    ResponseEntity.ok("Não encontramos nenhum veículo");
        }
        else{
            List<Veiculo> resposta = repository.findByPlacaContaining(placa);
            return !resposta.isEmpty() ?
                    ResponseEntity.ok(resposta) :
                    ResponseEntity.ok("Não encontramos nenhum veículo");
        }
    }

    @Override
    public ResponseEntity buscaVeiculosComIntervaloAquisicao(String dataInicial, String dataFinal) {
        //return repository.findByintevalo(datainicio, datafim);
        return null;
    }

    @Override
    public ResponseEntity alterarDadosVeiculo(Veiculo veiculo) {
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
        return null;
    }

    @Override
    public ResponseEntity deletarVeiculo(String renavam) {
        //repository.deleteById(renavam);
        return null;
    }

    @Override
    public boolean validaRenavam(String renavam){
        return renavam.matches("(?=.*\\d).{11}") && !renavam.matches("(?=.*[a-zA-Z]).+");
    }


    @Override
    public boolean validaUf(String uf) {
        return !uf.matches("(?=.*\\d).+") && uf.matches("(?=.*[a-zA-Z]).{2}");
    }

    @Override
    public boolean ufExistente(String uf) {
        for(Uf existente: Uf.values()){
            if(existente.toString().equals(uf)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean validaPlaca(String placaVeiculo) {
        return placaVeiculo.matches("[A-Z]{3}\\d[A-Z]\\d{2}|[A-Z]{3}\\d{4}");
    }

    @Override
    public boolean verificaTamanhoDaPlaca(String placaVeiculo){
        return placaVeiculo.length() == 7;
    }
}
