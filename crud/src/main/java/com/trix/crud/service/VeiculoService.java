package com.trix.crud.service;

import com.trix.crud.dto.NovoVeiculo;
import com.trix.crud.modelo.Uf;
import com.trix.crud.modelo.Veiculo;
import com.trix.crud.repository.VeiculoRepository;
import com.trix.crud.service.interfaces.ValidacoesVeiculosInterface;
import com.trix.crud.service.interfaces.VeiculoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Service
public class VeiculoService implements VeiculoInterface, ValidacoesVeiculosInterface{

    @Autowired
    VeiculoRepository repository;

    @Override
    public ResponseEntity cadastrarNovoVeiculo(NovoVeiculo novoVeiculo, URI uri) {
        if(validacoesParaCadastroVeiculo(
                novoVeiculo.getRenavamNovoVeiculo(),
                novoVeiculo.getPlacaNovoVeiculo(),
                novoVeiculo.getUfPlacaNovoVeiculo())
        ){
            repository.save(criaVeiculo(novoVeiculo));
            return ResponseEntity.created(uri).build();
        }

        return ResponseEntity.ok("Cadastro não realizado!");
    }

    @Override
    public Veiculo criaVeiculo(NovoVeiculo novoVeiculo) {

        Veiculo veiculo = new Veiculo();
        veiculo.setRenavam(novoVeiculo.getRenavamNovoVeiculo());
        veiculo.setChassi(novoVeiculo.getChassiNovoVeiculo().toUpperCase());
        veiculo.setUfPlaca(novoVeiculo.getUfPlacaNovoVeiculo().toUpperCase());
        veiculo.setPlaca(novoVeiculo.getPlacaNovoVeiculo());
        veiculo.setAnoFabricacao(novoVeiculo.getAnoFabricacaoNovoVeiculo());
        veiculo.setAnoModelo(novoVeiculo.getAnoModeloNovoVeiculo());
        veiculo.setDataAquisicao(novoVeiculo.getDataAquisicaoNovoVeiculo());
        veiculo.setCor(novoVeiculo.getCorNovoVeiculo());
        veiculo.setCnhCondutor("");
        return veiculo;
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
    public ResponseEntity alterarDadosVeiculo(NovoVeiculo veiculo) {
        if(repository.findById(veiculo.getRenavamNovoVeiculo()).isPresent()){
            Veiculo existente = repository.findById(veiculo.getRenavamNovoVeiculo()).get();
            repository.save(atualizacaoDados(veiculo,existente));
            return ResponseEntity.ok("Alterações salvas com sucesso");
        }
        return ResponseEntity.ok("Não existe nenhum veiculo com o renavam informado!");
    }

    @Override
    public Veiculo atualizacaoDados(NovoVeiculo veiculo, Veiculo existente) {

        existente.setPlaca(veiculo.getPlacaNovoVeiculo());
        existente.setChassi(veiculo.getChassiNovoVeiculo());
        existente.setAnoModelo(veiculo.getAnoModeloNovoVeiculo());
        existente.setAnoFabricacao(veiculo.getAnoFabricacaoNovoVeiculo());
        existente.setCor(veiculo.getCorNovoVeiculo());
        existente.setUfPlaca(veiculo.getUfPlacaNovoVeiculo());
        existente.setDataAquisicao(veiculo.getDataAquisicaoNovoVeiculo());

        return existente;
    }

    @Override
    public ResponseEntity deletarVeiculo(String renavam) {
        if(validaRenavam(renavam)){
            if(repository.findById(renavam).isPresent()){
                repository.deleteById(renavam);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Informe um renavam válido!");
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

    @Override
    public boolean veiculoNaoExiste(String renavam){
        return repository.findById(renavam).isEmpty();
    }

    @Override
    public boolean validacoesParaCadastroVeiculo(String renavam, String placa, String ufPlaca){
        return veiculoNaoExiste(renavam) &&
                validaRenavam(renavam) &&
                validaPlaca(placa) &&
                validaUf(ufPlaca) &&
                ufExistente(ufPlaca);
    }
}
