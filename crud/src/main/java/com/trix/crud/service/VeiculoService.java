package com.trix.crud.service;

import com.trix.crud.dto.NovoVeiculo;
import com.trix.crud.modelo.Uf;
import com.trix.crud.modelo.Veiculo;
import com.trix.crud.repository.VeiculoRepository;
import com.trix.crud.service.interfaces.VeiculoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class VeiculoService implements VeiculoInterface{

    @Autowired
    VeiculoRepository repository;
    private static final String FORMATO_DATA = "yyyy-MM-dd";

    @Override
    public ResponseEntity cadastrarNovoVeiculo(NovoVeiculo novoVeiculo, URI uri) {
        if (validacoesParaCadastroVeiculo(
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
        veiculo.setDataAquisicao(dataDaAquisicao());
        veiculo.setCor(novoVeiculo.getCorNovoVeiculo());
        veiculo.setCnhCondutor("");
        return veiculo;
    }

    @Override
    public List<Veiculo> findAll() {
        Iterable<Veiculo> iterableVeiculo = repository.findAll();
        return StreamSupport.stream(iterableVeiculo.spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity buscaVeiculoComRenavam(String renavam) {
        if (validaRenavam(renavam)){
            if (repository.findById(renavam).isPresent()){
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
        if (validaUf(ufDaPlaca) && ufExistente(ufDaPlaca)){
            List<Veiculo> resposta = repository.findByufPlaca(ufDaPlaca);
            if (!resposta.isEmpty()){
                return ResponseEntity.ok(resposta);
            } else {
                return ResponseEntity.ok("Não encontramos nenhum veículo dessa localidade");
            }
        }
        return ResponseEntity.ok("Digite uma localidade válida");
    }

    @Override
    public ResponseEntity buscaVeiculoComPlaca(String placa) {
        if (verificaTamanhoDaPlaca(placa)){
            return (validaPlaca(placa)) ? ResponseEntity.ok(repository.findByPlaca(placa)) :
                    ResponseEntity.ok("Não encontramos nenhum veículo");
        } else {
            List<Veiculo> resposta = repository.findByPlacaContaining(placa);
            return !resposta.isEmpty() ?
                    ResponseEntity.ok(resposta) :
                    ResponseEntity.ok("Não encontramos nenhum veículo");
        }
    }

    @Override
    public ResponseEntity buscaVeiculosComIntervaloAquisicao(String dataInicial, String dataFinal) {
        if (dataInicial.matches("(\\d{2}-\\d{2}-\\d{4})") && dataFinal.matches("(\\d{2}-\\d{2}-\\d{4})")
                && validaData(dataInicial) && validaData(dataFinal)){
            return ResponseEntity.ok(repository.findByintevalo(converteStringtoData(dataInicial), converteStringtoData(dataFinal)));
        }
        return ResponseEntity.ok("Por favor informe uma data válida! Ex: 31-12-2022");
    }

    @Override
    public ResponseEntity alterarDadosVeiculo(NovoVeiculo veiculo) {
        if (repository.findById(veiculo.getRenavamNovoVeiculo()).isPresent()){
            Veiculo existente = repository.findById(veiculo.getRenavamNovoVeiculo()).get();
            repository.save(atualizacaoDados(veiculo, existente));
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

        return existente;
    }

    @Override
    public ResponseEntity deletarVeiculo(String renavam) {
        if (validaRenavam(renavam)){
            if (repository.findById(renavam).isPresent()){
                repository.deleteById(renavam);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Informe um renavam válido!");
    }

    @Override
    public boolean validaRenavam(String renavam) {
        return renavam.matches("(?=.*\\d).{11}") && !renavam.matches("(?=.*[a-zA-Z} {,.^?~=+_/*|]).+");
    }


    @Override
    public boolean validaUf(String uf) {
        return !uf.matches("(?=.*\\d).+") && uf.matches("(?=.*[a-zA-Z]).{2}");
    }

    @Override
    public boolean ufExistente(String uf) {
        for (Uf existente : Uf.values()){
            if (existente.toString().equals(uf)){
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
    public boolean verificaTamanhoDaPlaca(String placaVeiculo) {
        return placaVeiculo.length() == 7;
    }

    @Override
    public boolean veiculoNaoExiste(String renavam) {
        return repository.findById(renavam).isEmpty();
    }

    @Override
    public boolean validacoesParaCadastroVeiculo(String renavam, String placa, String ufPlaca) {
        return veiculoNaoExiste(renavam) &&
                validaRenavam(renavam) &&
                validaPlaca(placa) &&
                validaUf(ufPlaca) &&
                ufExistente(ufPlaca);
    }

    @Override
    public boolean verificacaoVeiculoParaAquisicao(String renavam) {
        if (validaRenavam(renavam) && !veiculoNaoExiste(renavam)){
            Veiculo veiculo = repository.findById(renavam).get();
            return veiculo.getCnhCondutor().equals("");
        }
        return false;
    }

    @Override
    public boolean atribuirCondutorAoVeiculo(String renavam, String cnh) {
        Veiculo veiculo = repository.findById(renavam).get();
        veiculo.setCnhCondutor(cnh);
        repository.save(veiculo);
        return true;
    }

    @Override
    public Veiculo LiberacaoVeiculo(String renavam) {
        Veiculo veiculo = repository.findById(renavam).get();
        veiculo.setCnhCondutor("");
        repository.save(veiculo);
        return veiculo;
    }

    @Override
    public boolean validaData(String data) {
        try{
            SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
            date.setLenient(false);
            date.parse(data);
            return true;
        } catch (ParseException e){
            return false;
        }
    }

    @Override
    public Date dataDaAquisicao() {
        try{
            SimpleDateFormat date = new SimpleDateFormat(FORMATO_DATA);
            date.setLenient(false);
            return date.parse(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        } catch (NullPointerException | ParseException e){
            return null;
        }
    }


    @Override
    public Date converteStringtoData(String data) {
        try{
            SimpleDateFormat date = new SimpleDateFormat(FORMATO_DATA);
            DateTimeFormatter dataFormatoInserido = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter dataFormatoNecessario = DateTimeFormatter.ofPattern(FORMATO_DATA);
            date.setLenient(false);
            return date.parse(LocalDate.parse(data, dataFormatoInserido).format(dataFormatoNecessario));
        } catch (DateTimeParseException | NullPointerException | ParseException e){
            return null;
        }
    }
}
