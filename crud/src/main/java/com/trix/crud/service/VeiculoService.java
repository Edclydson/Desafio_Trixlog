package com.trix.crud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trix.crud.dto.NovoVeiculo;
import com.trix.crud.modelo.Condutor;
import com.trix.crud.modelo.Veiculo;
import com.trix.crud.repository.VeiculoRepository;

@Service
public class VeiculoService {

    @Autowired
    VeiculoRepository repository;

    @Autowired
    CondutorService condutorS;

    public void registraNovoVeiculo(NovoVeiculo novoVeiculo){
        if(buscaveiculo(novoVeiculo.getRenavamNovoVeiculo()).isPresent() == false){
            
            Veiculo veiculo = new Veiculo();
            veiculo.setRenavam(novoVeiculo.getRenavamNovoVeiculo());
            veiculo.setChassi(novoVeiculo.getChassiNovoVeiculo().toUpperCase());
            veiculo.setUfPlaca(novoVeiculo.getUfPlacaNovoVeiculo());
            veiculo.setPlaca(novoVeiculo.getPlacaNovoVeiculo());
            veiculo.setAnoFabricacao(novoVeiculo.getAnoFabricacaoNovoVeiculo());
            veiculo.setAnoModelo(novoVeiculo.getAnoModeloNovoVeiculo());
            veiculo.setDataAquisicao(novoVeiculo.getDataAquisicaoNovoVeiculo());
            veiculo.setCor(novoVeiculo.getCorNovoVeiculo());

            if(novoVeiculo.getCnhCondutorNovoVeiculo().equals("") && novoVeiculo.getNomeCondutorNovoVeiculo().equals("")){
                veiculo.setCnhCondutor("");
                veiculo.setNomeCondutor("");
                repository.save(veiculo);
            }
            else{
                veiculo.setCnhCondutor(novoVeiculo.getCnhCondutorNovoVeiculo());
                veiculo.setNomeCondutor(novoVeiculo.getNomeCondutorNovoVeiculo());
                repository.save(veiculo);
                
                Condutor condutorComVeiculo = condutorS.consultaCondutorcnh(novoVeiculo.getCnhCondutorNovoVeiculo()).get();
                condutorComVeiculo.getListaDeVeiculos().add(veiculo);
                condutorS.addVeiculoCondutor(condutorComVeiculo);
            }
        }
    }    

    public List<Veiculo> findAll(){
        return (List<Veiculo>) repository.findAll();
    }

    public Optional<Veiculo> buscaveiculo(String renavam){
        return repository.findById(renavam);
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
                Condutor condutorsemV = condutorS.consultaCondutorcnh(condutorApagar).get();
                condutorsemV.getListaDeVeiculos().remove(existente);
                condutorS.rmvVeiculoCondutor(condutorsemV);
            }else{
                Condutor condutorComVeiculo = condutorS.consultaCondutorcnh(veiculo.getCnhCondutor()).get();
                condutorComVeiculo.getListaDeVeiculos().add(veiculo);
                condutorS.addVeiculoCondutor(condutorComVeiculo);
            }
            
            
            repository.save(existente);
    }

    public void deletaveiculo(String renavam){
        repository.deleteById(renavam);
    }

    public List<Veiculo> buscaveiculoufplaca(String uf){
        return repository.findByufPlaca(uf);

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
}
